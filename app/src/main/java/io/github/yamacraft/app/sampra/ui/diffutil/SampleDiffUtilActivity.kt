package io.github.yamacraft.app.sampra.ui.diffutil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.yamacraft.app.sampra.R
import kotlinx.android.synthetic.main.activity_list_adapter.*

class SampleDiffUtilActivity : AppCompatActivity() {

    private lateinit var viewModel: SampleDiffUtilViewModel
    private lateinit var listAdapter: SampleDiffUtilListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_adapter)

        viewModel = ViewModelProviders.of(this)[SampleDiffUtilViewModel::class.java]
        viewModel.apply {
            progress.observe(this@SampleDiffUtilActivity, Observer {
                runOnUiThread {
                    refresh.isRefreshing = it
                }
            })
            items.observe(this@SampleDiffUtilActivity, Observer {
                listAdapter.submitList(it)
            })
        }
        listAdapter = SampleDiffUtilListAdapter().apply {
            onItemClickListener = {
                viewModel.itemCountUp(it.id)
            }
        }

        recycler_view.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@SampleDiffUtilActivity,
                    LinearLayoutManager(context).orientation
                )
            )
        }
        refresh.apply {
            setOnRefreshListener {
                viewModel.refresh()
            }
        }
    }
}
