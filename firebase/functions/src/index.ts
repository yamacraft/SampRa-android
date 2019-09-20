// https://github.com/FirebaseExtended/custom-auth-samples/blob/master/Line/server/app.js
'use strict';

import * as functions from 'firebase-functions';
import { FirebaseError } from 'firebase-admin';

// Modules imports
const rp = require('request-promise');
const express = require('express');
const bodyParser = require('body-parser');

// Firebase Setup
// service-account.jsonはlib配下に置く
const admin = require('firebase-admin');
const serviceAccount = require('./service-account.json');
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

function generateTwitchApiRequest(apiEndpoint: string, accessToken: string) {
    return {
        url: apiEndpoint,
        headers: {
            'Authorization': `OAuth ${accessToken}`
        },
        json: true
    };
}

// Firebase登録情報の取得
function getFirebaseUser(uid: string, accessToken: string) {
    return admin.auth().getUser(uid).catch((error: FirebaseError) => {
        // まだ作成されていない場合は、下記の処理を実施
        if (error.code === 'auth/user-not-found') {
            const getProfileOptions 
                = generateTwitchApiRequest('https://id.twitch.tv/oauth2/validate', accessToken);
            return rp(getProfileOptions).then((response: {login:string, user_id:string}) => {
                const twitchUid = response.user_id;
                const displayName = response.login;
        
                console.log(`Twitch displayName:${displayName}`)
                console.log('Create new Firebase user for Twitch user uid = "', twitchUid,'"');
                
                // 作成したUser情報を登録して返却
                return admin.auth().createUser({
                    uid: twitchUid,
                    displayName: displayName
                });
            });
        }
        throw error;
    });
}

// accessToken検証
function verifyTwitchToken(accessToken: string) {

    // 外部アクセスするので有料プラン（Flame or Blaze）必須
    const verifyTokenOptions 
        = generateTwitchApiRequest('https://id.twitch.tv/oauth2/validate', accessToken);
    return rp(verifyTokenOptions)
        .then((response: { user_id: string; }) => {
            // 本来ならClientIdのチェックを行う
            const uid = response.user_id;
            return getFirebaseUser(uid, accessToken);
        })
        .then((userRecord: { uid: string; }) => {
            // Firebase AuthenticationのaccessTokenを取得して返す
            const tokenPromise = admin.auth().createCustomToken(userRecord.uid);
            tokenPromise.then( (token: string) => {
                console.log(
                    'Created Custom token for UID "', userRecord.uid, '" Token:', token);
            });
        return tokenPromise;
    });
}
  
// ExpressJS setup
const app = express();
app.use(bodyParser.json());

// POST /verifyTwitch
export const verifyTwitch = functions.https.onRequest((request, response) => {
    if(request.body.token === undefined) {
        const ret = {
            error_message: 'Access Token not found'
        };
        return response.status(400).send(ret);
    }

    const reqToken = request.body.token;

    verifyTwitchToken(reqToken)
        .then((customAuthToken: string) => {
            const ret = {
                firebase_token: customAuthToken
            };
            return response.status(200).send(ret);   
        })
        .catch((err: FirebaseError)=> {
            const ret = {
                error_message: 'Authentication error: Cannot verify access token.'
            };
            return response.status(403).send(ret);
        });
    
    return response.status(400);
});

export const helloWorld = functions.https.onRequest((request, response) => {
    response.send('Hello from Firebase!\n\n');
});
