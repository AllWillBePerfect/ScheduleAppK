package com.schedule.views.adapter.adaptersdelegate

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder

interface AdapterItemDelegate<T> {
    fun forItem(item: T): Boolean
    fun getViewHolder(parent: ViewGroup): ViewHolder
    fun bindViewHolder(viewHolder: ViewHolder, item: T, payloads: MutableList<Any>)
}




