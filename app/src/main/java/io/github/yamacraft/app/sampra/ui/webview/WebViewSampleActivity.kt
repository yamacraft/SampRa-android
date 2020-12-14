package io.github.yamacraft.app.sampra.ui.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import io.github.yamacraft.app.sampra.databinding.ActivityWebviewSampleBinding

class WebViewSampleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebviewSampleBinding

    @SuppressLint("JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityWebviewSampleBinding.inflate(layoutInflater).apply {
            binding = this
            setContentView(binding.root)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        @SuppressLint("SetJavaScriptEnabled")
        binding.webView.settings.javaScriptEnabled = true

        binding.webView.apply {
            addJavascriptInterface(this@WebViewSampleActivity, JS_INTERFACE_NAME)

            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    binding.progressBar.isVisible = true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.progressBar.isVisible = false
                    view?.loadUrl(JS_CODE_GET_INNER_HTML)
                    super.onPageFinished(view, url)
                }
            }

            loadUrl(LOAD_URL)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Suppress("unused")
    @JavascriptInterface
    fun processJs(text: String?) {
        runOnUiThread {
            val source = """
            ${binding.webView.settings.userAgentString}
            -----
            ${text.orEmpty()}
            """.trimIndent()

            binding.sourceTextView.text = source
        }
    }

    companion object {
        private const val LOAD_URL = "https://yamacraft.github.io/"

        private const val JS_INTERFACE_NAME = "Android"

        // htmlソースを取得する
        private const val JS_CODE_GET_INNER_HTML =
            "javascript:${JS_INTERFACE_NAME}.processJs(document.getElementsByTagName('html')[0].outerHTML);"
    }
}
