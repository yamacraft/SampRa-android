package io.github.yamacraft.app.sampra.ui.auth.signin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.OAuthProvider
import io.github.yamacraft.app.sampra.R
import kotlinx.android.synthetic.main.activity_auth_twitch.*
import timber.log.Timber

class AuthTwitterActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthTwitterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_twitter)

        viewModel = ViewModelProviders.of(this)[AuthTwitterViewModel::class.java]

        viewModel.apply {
            log.observe(this@AuthTwitterActivity, Observer {
                log_text.text = it
            })

            progress.observe(this@AuthTwitterActivity, Observer {
                progress_bar.isVisible = it
                sign_in_button.isEnabled = !it
            })
        }

        sign_in_button.setOnClickListener {
            val provider = OAuthProvider.newBuilder("twitter.com")
                .addCustomParameter("language", "ja")
            viewModel.getAuth()
                .startActivityForSignInWithProvider(this, provider.build())
                .addOnSuccessListener {
                    Timber.d("success")
                }
                .addOnFailureListener {
                    Timber.e(it)
                }
        }

        viewModel.onCreated()
    }
}
