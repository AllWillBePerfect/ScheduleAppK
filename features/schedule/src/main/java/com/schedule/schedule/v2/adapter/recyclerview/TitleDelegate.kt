package com.schedule.schedule.v2.adapter.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.schedule.schedule.databinding.V2ItemTitleBinding
import com.schedule.schedule.v2.adapter.recyclerview.model.TimetableItem
import com.schedule.views.adapter.adaptersdelegate.AdapterItemDelegate

class TitleDelegate : AdapterItemDelegate<TimetableItem> {
    override fun forItem(item: TimetableItem): Boolean = item is TimetableItem.Title

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
//        Log.d("TimetableItem", "TitleDelegate create")
        val layoutInflater = LayoutInflater.from(parent.context)
        return TitleViewHolder(V2ItemTitleBinding.inflate(layoutInflater, parent, false))
    }

    override fun bindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: TimetableItem,
        payloads: MutableList<Any>
    ) {
//        Log.d("TimetableItem", "TitleDelegate bind")
        (viewHolder as TitleViewHolder).bind(item as TimetableItem.Title)
    }

    inner class TitleViewHolder(private val binding: V2ItemTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TimetableItem.Title) {
            binding.dateTextView.text = item.date
            binding.dayOfWeekNameTextView.text = item.dayOfWeekName
            binding.groupNameTextView.text = item.groupName
            binding.groupNameTextView.visibility =
                if (item.isTitleEnabled) View.VISIBLE else View.GONE

        }
    }
}