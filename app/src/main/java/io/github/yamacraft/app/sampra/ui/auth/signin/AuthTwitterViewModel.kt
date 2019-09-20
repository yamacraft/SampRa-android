package io.github.yamacraft.app.sampra.ui.auth.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthTwitterViewModel : ViewModel(), FirebaseAuth.AuthStateListener {

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress
    private val _log = MutableLiveData<String>()
    val log: LiveData<String> = _log

    private val auth = FirebaseAuth.getInstance()

    override fun onCleared() {
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
        auth.addAuthStateListener(this)
    }

    private fun appendLog(text: String) {
        _log.value = _log.value?.plus("\n$text") ?: text
    }

    private fun clearLog() {
        _log.value = ""
    }

    fun getAuth(): FirebaseAuth = auth
}
