package io.github.yamacraft.app.sampra.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import io.github.yamacraft.app.sampra.data.AuthUserData
import timber.log.Timber
import java.util.*

class AuthViewModel : ViewModel(), FirebaseAuth.AuthStateListener {

    private val _firebaseUser = MutableLiveData<FirebaseUser>()
    val firebaseUser: LiveData<FirebaseUser> = _firebaseUser
    private val _progress = MutableLiveData<Boolean>().apply { value = false }
    val progress: LiveData<Boolean> = _progress
    private val _log = MutableLiveData<String>()
    val log: LiveData<String> = _log

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCleared() {
        auth.removeAuthStateListener(this)
        super.onCleared()
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        _firebaseUser.value = firebaseAuth.currentUser
        appendLog("onAuthStateChanged")
        putAuthUserData()
    }

    fun onCreated() {
        auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener(this)
        db = FirebaseFirestore.getInstance()
    }

    fun onUserUpdate(name: String) {
        auth.currentUser?.run {
            _progress.value = true
            val updateProfile = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            this.updateProfile(updateProfile)
                .addOnCompleteListener {
                    _progress.value = false
                    appendLog("UpdateProfile: ${it.isSuccessful}")
                }
        }
    }

    fun onSignOut() {
        auth.signOut()
    }

    private fun appendLog(text: String) {
        _log.value = _log.value?.plus("\n$text") ?: text
    }

    private fun clearLog() {
        _log.value = ""
    }

    private fun putAuthUserData() {
        auth.currentUser?.let { user ->
            val userData = AuthUserData(user.displayName.orEmpty(), Date().time)
            db.collection("lastLogin")
                .document(user.uid)
                .set(userData)
                .addOnSuccessListener {
                    appendLog("success FireStore write")
                }
                .addOnFailureListener {
                    appendLog("error FireStore write")
                    Timber.e(it)
                }
        }
    }
}
