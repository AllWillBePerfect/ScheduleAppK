package com.example.schedule.v2.adapter.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.databinding.V2ItemTitleCurrentBinding
import com.example.schedule.v2.adapter.recyclerview.model.TimetableItem
import com.example.views.adapter.adaptersdelegate.AdapterItemDelegate

class TitleCurrentDelegate: AdapterItemDelegate<TimetableItem> {
    override fun forItem(item: TimetableItem): Boolean = item is TimetableItem.TitleCurrent

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        Log.d("TimetableItem", "TitleCurrentDelegate create")
        val layoutInflater = LayoutInflater.from(parent.context)
        return TitleCurrentViewHolder(V2ItemTitleCurrentBinding.inflate(layoutInflater, parent, false))
    }

    override fun bindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: TimetableItem,
        payloads: MutableList<Any>
    ) {
        Log.d("TimetableItem", "TitleCurrentDelegate bind")

        (viewHolder as TitleCurrentViewHolder).bind(item as TimetableItem.TitleCurrent)
    }

    inner class TitleCurrentViewHolder(private val binding: V2ItemTitleCurrentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TimetableItem.TitleCurrent) {
            binding.dateTextView.text = item.date
            binding.dayOfWeekNameTextView.text = item.dayOfWeekName
            binding.groupNameTextView.text = item.groupName
        }
    }
}