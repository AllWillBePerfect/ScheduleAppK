package com.example.schedule.v2.adapter.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.databinding.V2ItemTitleBinding
import com.example.schedule.v2.adapter.recyclerview.model.TimetableItem
import com.example.views.adapter.adaptersdelegate.AdapterItemDelegate

class TitleDelegate: AdapterItemDelegate<TimetableItem> {
    override fun forItem(item: TimetableItem): Boolean = item is TimetableItem.Title

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        Log.d("TimetableItem", "TitleDelegate create")
        val layoutInflater = LayoutInflater.from(parent.context)
        return TitleViewHolder(V2ItemTitleBinding.inflate(layoutInflater, parent, false))
    }

    override fun bindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: TimetableItem,
        payloads: MutableList<Any>
    ) {
        Log.d("TimetableItem", "TitleDelegate bind")
        (viewHolder as TitleViewHolder).bind(item as TimetableItem.Title)
    }

    inner class TitleViewHolder(private val binding: V2ItemTitleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TimetableItem.Title) {
            binding.dateTextView.text = item.date
            binding.dayOfWeekNameTextView.text = item.dayOfWeekName
            binding.groupNameTextView.text = item.groupName
        }
    }
}