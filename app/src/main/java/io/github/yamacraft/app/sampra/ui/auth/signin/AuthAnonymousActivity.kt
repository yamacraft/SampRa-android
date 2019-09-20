package io.github.yamacraft.app.sampra.ui.auth.signin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.OAuthProvider
import io.github.yamacraft.app.sampra.BuildConfig
import io.github.yamacraft.app.sampra.R
import kotlinx.android.synthetic.main.activity_auth_anonymous.*
import timber.log.Timber

class AuthAnonymousActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthAnonymousViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_anonymous)

        viewModel = ViewModelProviders.of(this)[AuthAnonymousViewModel::class.java]

        viewModel.apply {
            log.observe(this@AuthAnonymousActivity, Observer {
                log_text.text = it
            })

            progress.observe(this@AuthAnonymousActivity, Observer {
                progress_bar.isVisible = it
                sign_in_button.isEnabled = !it
            })

            user.observe(this@AuthAnonymousActivity, Observer {
                link_button.isEnabled = it.isAnonymous
            })
        }

        sign_in_button.setOnClickListener {
            viewModel.onSignIn()
        }

        link_button.setOnClickListener {
            val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_AUTH_WEB_CLIENT_ID)
                .build()
            val googleSignInClient = GoogleSignIn.getClient(this, options)

            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }

        link2_button.setOnClickListener {
            val provider = OAuthProvider.newBuilder("twitter.com")
                .addCustomParameter("language", "ja")
                .build()
            viewModel.getAuth().currentUser?.run {
                startActivityForLinkWithProvider(this@AuthAnonymousActivity, provider)
                    .addOnSuccessListener {
                        Timber.d("success")
                    }
                    .addOnFailureListener {
                        Timber.e(it)
                    }
            }
        }

        viewModel.onCreated()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN
            && resultCode == Activity.RESULT_OK
            && data != null
        ) {
            viewModel.onCallbackUri(data)
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
