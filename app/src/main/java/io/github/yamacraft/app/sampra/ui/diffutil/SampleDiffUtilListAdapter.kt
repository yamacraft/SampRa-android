package io.github.yamacraft.app.sampra.ui.diffutil

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.yamacraft.app.sampra.R

class SampleDiffUtilListAdapter :
    ListAdapter<SampleDiffUtilListAdapter.SampleDillUtilItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    var onItemClickListener: ((SampleDillUtilItem) -> Unit)? = null

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SampleDillUtilItem>() {
            override fun areItemsTheSame(
                oldItem: SampleDillUtilItem,
                newItem: SampleDillUtilItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SampleDillUtilItem,
                newItem: SampleDillUtilItem
            ): Boolean {
                return oldItem.count == newItem.count
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.listitem_list_adapter, parent, false)
        return SampleItemViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is SampleItemViewHolder -> {

                holder.apply {
                    name.text = "${item.id}(${item.count})"
                    itemView.setOnClickListener {
                        onItemClickListener?.invoke(item)
                    }
                }
            }
        }
    }

    private class SampleItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
    }

    data class SampleDillUtilItem(
        val id: Int,
        var count: Int
    )
}
