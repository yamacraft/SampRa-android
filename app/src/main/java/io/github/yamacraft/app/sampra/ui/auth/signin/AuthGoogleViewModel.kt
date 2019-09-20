package io.github.yamacraft.app.sampra.ui.auth.signin

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthGoogleViewModel : ViewModel(), FirebaseAuth.AuthStateListener {

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress
    private val _log = MutableLiveData<String>()
    val log: LiveData<String> = _log

    private lateinit var auth: FirebaseAuth

    override fun onCleared() {
        auth.removeAuthStateListener(this)
        super.onCleared()
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        firebaseAuth.currentUser?.let {
            appendLog("getUser -> ${it.uid}:${it.displayName}:${it.photoUrl?.toString()}")
        }
    }

    fun onCreated() {
        clearLog()
        appendLog("** Start **")
        auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener(this)
    }

    fun onCallbackUri(intent: Intent) {
        _progress.value = true
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        try {
            task.getResult(ApiException::class.java)?.let { account ->
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        _progress.value = false
                        appendLog("signIn Successful:${task.isSuccessful}")
                    }
            }
        } catch (exception: ApiException) {
            appendLog("exception:$exception")
            _progress.value = false
        }
    }

    private fun appendLog(text: String) {
        _log.value = _log.value?.plus("\n$text") ?: text
    }

    private fun clearLog() {
        _log.value = ""
    }
}
