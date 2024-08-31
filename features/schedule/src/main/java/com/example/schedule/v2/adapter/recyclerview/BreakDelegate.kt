package com.example.schedule.v2.adapter.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.databinding.V2ItemLessonBreakBinding
import com.example.schedule.v2.adapter.recyclerview.model.TimetableItem
import com.example.views.adapter.adaptersdelegate.AdapterItemDelegate

class BreakDelegate : AdapterItemDelegate<TimetableItem> {
    override fun forItem(item: TimetableItem): Boolean = item is TimetableItem.Break

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        Log.d("TimetableItem", "BreakDelegate create")
        val layoutInflater = LayoutInflater.from(parent.context)
        return BreakViewHolder(V2ItemLessonBreakBinding.inflate(layoutInflater, parent, false))
    }

    override fun bindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: TimetableItem,
        payloads: MutableList<Any>
    ) {
        Log.d("TimetableItem", "BreakDelegate bind")
        (viewHolder as BreakViewHolder).bind(item as TimetableItem.Break)
    }

    inner class BreakViewHolder(private val binding: V2ItemLessonBreakBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TimetableItem.Break) {
            binding.timeTextView.text = item.time
            binding.lessonTextView.text = item.lessonName
            binding.progressIndicator.progress = item.progressValue
        }
    }
}