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
import io.github.yamacraft.app.sampra.BuildConfig
import io.github.yamacraft.app.sampra.R
import kotlinx.android.synthetic.main.activity_auth_google.*

class AuthGoogleActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthGoogleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_google)

        viewModel = ViewModelProviders.of(this)[AuthGoogleViewModel::class.java]

        viewModel.apply {
            log.observe(this@AuthGoogleActivity, Observer {
                log_text.text = it
            })
            progress.observe(this@AuthGoogleActivity, Observer {
                progress_bar.isVisible = it
                sign_in_button.isEnabled = !it
            })
        }

        sign_in_button.setOnClickListener {
            val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_AUTH_WEB_CLIENT_ID)
                //.requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(this, options)

            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
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
