package io.github.yamacraft.app.sampra.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.yamacraft.app.sampra.R
import io.github.yamacraft.app.sampra.data.ClassItem

class MainRecyclerViewAdapter(private val items: List<ClassItem>, private val listener: OnItemClickListener?) :
    RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameTextView.apply {
            text = items[position].name
            setOnClickListener {
                listener?.run {
                    onItemClick(items[holder.adapterPosition])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listitem_menu, parent, false)
        return ViewHolder(view)
    }

    interface OnItemClickListener {
        fun onItemClick(classMenu: ClassItem)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.name)
    }
}
