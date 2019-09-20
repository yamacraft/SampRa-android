package io.github.yamacraft.app.sampra.ui.auth.signin

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import io.github.yamacraft.app.sampra.api.AuthFunctionApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class AuthTwitchViewModel : ViewModel(), CoroutineScope, FirebaseAuth.AuthStateListener {

    override val coroutineContext: CoroutineContext = Dispatchers.Default

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress
    private val _log = MutableLiveData<String>()
    val log: LiveData<String> = _log

    private var validateJob: Job? = null

    private lateinit var auth: FirebaseAuth

    override fun onCleared() {
        validateJob?.cancel()
        auth.removeAuthStateListener(this)
        super.onCleared()
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        firebaseAuth.currentUser?.let {
            appendLog("getUser -> ${it.uid}:${it.displayName}")
        }
    }

    fun onCreated() {
        clearLog()
        appendLog("** Start **")
        auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener(this)
    }

    fun onCallbackUri(uri: Uri) {
        _progress.value = true
        Timber.d("uri:$uri")
        val accessToken = uri.toString()
            .replace("oauth#", "oauth?")
            .toUri()
            .getQueryParameter("access_token")
            .orEmpty()

        appendLog(uri.toString())
        appendLog("code: $accessToken")

        validateJob = launch {
            val response = AuthFunctionApiClient
                .create()
                .verify(accessToken)
                .execute()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    appendLog("success!")
                    response.body()?.let {
                        appendLog("FirebaseToken:${it.firebaseToken}")
                        firebaseSignIn(it.firebaseToken)
                        _progress.value = true
                    }
                } else {
                    appendLog("${response.code()}:${response.message()}")
                    _progress.value = false
                }
            }
        }
    }

    private fun firebaseSignIn(token: String) {
        _progress.value = true
        auth.signInWithCustomToken(token)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    appendLog("success -> ${auth.currentUser?.uid}")
                } else {
                    appendLog("failure -> ${it.exception}")
                }
                _progress.value = false
            }
    }

    private fun appendLog(text: String) {
        _log.value = _log.value?.plus("\n$text") ?: text
        Timber.d(text)
    }

    private fun clearLog() {
        _log.value = ""
    }
}
