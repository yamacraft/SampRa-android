package io.github.yamacraft.app.sampra.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.yamacraft.app.sampra.data.ClassItem
import io.github.yamacraft.app.sampra.databinding.ActivityMainBinding
import io.github.yamacraft.app.sampra.ui.auth.AuthActivity
import io.github.yamacraft.app.sampra.ui.diffutil.SampleDiffUtilActivity
import io.github.yamacraft.app.sampra.ui.webview.WebViewSampleActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).apply {
            binding = this
            setContentView(binding.root)
        }

        binding.menu.adapter = MainRecyclerViewAdapter(
            getMenu(),
            object : MainRecyclerViewAdapter.OnItemClickListener {
                override fun onItemClick(classMenu: ClassItem) {
                    val intent = Intent().apply {
                        setClassName(this@MainActivity, classMenu.className)
                        action = Intent.ACTION_VIEW
                    }
                    startActivity(intent)
                }
            })
    }

    private fun getMenu(): List<ClassItem> {
        val items = mutableListOf<ClassItem>()
        items.add(
            ClassItem(
                AuthActivity::class.java.simpleName,
                AuthActivity::class.java.name
            )
        )
        items.add(
            ClassItem(
                SampleDiffUtilActivity::class.java.simpleName,
                SampleDiffUtilActivity::class.java.name
            )
        )
        items.add(
            ClassItem(
                WebViewSampleActivity::class.java.simpleName,
                WebViewSampleActivity::class.java.name
            )
        )
        return items.toList()
    }
}
