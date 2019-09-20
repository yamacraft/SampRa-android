package io.github.yamacraft.app.sampra.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.github.yamacraft.app.sampra.R
import io.github.yamacraft.app.sampra.ui.auth.signin.AuthAnonymousActivity
import io.github.yamacraft.app.sampra.ui.auth.signin.AuthGoogleActivity
import io.github.yamacraft.app.sampra.ui.auth.signin.AuthTwitchActivity
import io.github.yamacraft.app.sampra.ui.auth.signin.AuthTwitterActivity
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewModel = ViewModelProviders.of(this)[AuthViewModel::class.java]

        viewModel.apply {
            firebaseUser.observe(this@AuthActivity, Observer {
                if (it == null) {
                    uid_text.text = "Not Login"
                    name_text.apply {
                        isEnabled = false
                        setText("")
                    }
                    email_text.apply {
                        setText("")
                    }
                    update_user_button.isEnabled = false
                } else {
                    uid_text.text = "${it.providerId} - ${it.uid}"
                    name_text.apply {
                        isEnabled = true
                        setText(it.displayName.orEmpty())
                    }
                    email_text.apply {
                        setText(it.email.orEmpty())
                    }
                    update_user_button.isEnabled = true
                }
            })

            log.observe(this@AuthActivity, Observer {
                log_text.text = it
            })

            progress.observe(this@AuthActivity, Observer {
                update_user_button.isEnabled = it.not()
            })
        }

        update_user_button.setOnClickListener {
            viewModel.onUserUpdate(name_text.text.toString())
        }

        sign_out_button.setOnClickListener {
            viewModel.onSignOut()
        }

        sign_in_google_button.setOnClickListener {
            val intent = Intent(this, AuthGoogleActivity::class.java).apply {
                action = Intent.ACTION_VIEW
            }
            startActivity(intent)
        }

        sign_in_twitter_button.setOnClickListener {
            val intent = Intent(this, AuthTwitterActivity::class.java).apply {
                action = Intent.ACTION_VIEW
            }
            startActivity(intent)
        }

        sign_in_twitch_button.setOnClickListener {
            val intent = Intent(this, AuthTwitchActivity::class.java).apply {
                action = Intent.ACTION_VIEW
            }
            startActivity(intent)
        }

        sign_in_anonymous_button.setOnClickListener {
            val intent = Intent(this, AuthAnonymousActivity::class.java).apply {
                action = Intent.ACTION_VIEW
            }
            startActivity(intent)
        }

        viewModel.onCreated()
    }
}
