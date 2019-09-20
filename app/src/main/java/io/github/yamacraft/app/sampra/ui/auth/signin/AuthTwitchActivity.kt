package io.github.yamacraft.app.sampra.ui.auth.signin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.github.yamacraft.app.sampra.BuildConfig
import io.github.yamacraft.app.sampra.R
import kotlinx.android.synthetic.main.activity_auth_twitch.*

class AuthTwitchActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthTwitchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_twitch)

        viewModel = ViewModelProviders.of(this)[AuthTwitchViewModel::class.java]

        viewModel.apply {
            log.observe(this@AuthTwitchActivity, Observer {
                log_text.text = it
            })

            progress.observe(this@AuthTwitchActivity, Observer {
                progress_bar.isVisible = it
                sign_in_button.isEnabled = !it
            })
        }

        sign_in_button.setOnClickListener {
            CustomTabsIntent.Builder()
                .build()
                .launchUrl(this@AuthTwitchActivity, authorizeUri())
        }

        viewModel.onCreated()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        intent.data?.let {
            viewModel.onCallbackUri(it)
        }
    }

    companion object {
        private fun authorizeUri() = "https://id.twitch.tv/"
            .plus("oauth2/authorize")
            .plus("?client_id=${BuildConfig.TWITCH_CLIENT_ID}")
            .plus("&redirect_uri=io.github.yamacraft.app.sampra://oauth")
            .plus("&response_type=token")
            .plus("&scope=user:edit")
            .toUri()
    }
}
