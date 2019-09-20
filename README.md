Samp-ra
=======

各動作検証用のAndroidプロジェクトです。

## 動作

- masterブランチではStableチャンネルでの最新版のAndroid Studioでの動作を前提としています
- ビルドにはFirebaseプロジェクトの用意が必要です

### 秘匿情報

- `local.properties`
- `app/google-service.json`
- `firebase/.firebaserc`

### local.properties追記情報

```
# Firebase Authentication（動作させる必要がなければ適当な値を入れてください）
TWITCH_CLIENT_ID=xxxxxxxxxxxxx
FUNCTIONS_BASE_URL=https://xxxxxxxx
GOOGLE_AUTH_WEB_CLIENT_ID=xxxxxxxxxxxxxx
```


