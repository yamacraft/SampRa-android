package io.github.yamacraft.app.sampra.ui.auth.signin

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class AuthAnonymousViewModel : ViewModel(), FirebaseAuth.AuthStateListener {

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress
    private val _log = MutableLiveData<String>()
    val log: LiveData<String> = _log
    private val _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser> = _user

    private val auth = FirebaseAuth.getInstance()
    fun getAuth() = auth

    override fun onCleared() {
        auth.removeAuthStateListener(this)
        super.onCleared()
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        // anonymous変更時は呼ばれない
        firebaseAuth.currentUser?.let {
            appendLog("getUser -> ${it.uid}:${it.displayName}")
            _user.value = it
        }
    }

    fun onCreated() {
        clearLog()
        appendLog("** Start **")
        auth.addAuthStateListener(this)
    }

    fun onSignIn() {
        _progress.value = true
        auth.signInAnonymously()
            .addOnCompleteListener {
                _progress.value = false
                appendLog("SignIn Successful:${it.isSuccessful}")
            }
    }

    fun onCallbackUri(intent: Intent) {
        _progress.value = true
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        try {
            task.getResult(ApiException::class.java)?.let { account ->
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.currentUser?.run {
                    linkWithCredential(credential)
                        .addOnCompleteListener { task ->
                            _progress.value = false
                            _user.value = this
                            appendLog("link Successful:${task.isSuccessful}")
                        }
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
