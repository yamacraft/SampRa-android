package io.github.yamacraft.app.sampra.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.yamacraft.app.sampra.R
import io.github.yamacraft.app.sampra.data.ClassItem
import io.github.yamacraft.app.sampra.ui.auth.AuthActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menu.adapter = MainRecyclerViewAdapter(getMenu(), object : MainRecyclerViewAdapter.OnItemClickListener {
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
        items.add(ClassItem(AuthActivity::class.java.simpleName, AuthActivity::class.java.name))
        return items.toList()
    }
}
