package com.example.schedule.v2.adapter.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.databinding.V2ItemLessonBinding
import com.example.schedule.v2.adapter.recyclerview.model.TimetableItem
import com.example.views.adapter.adaptersdelegate.AdapterItemDelegate

class LessonDelegate: AdapterItemDelegate<TimetableItem> {
    override fun forItem(item: TimetableItem): Boolean = item is TimetableItem.Lesson

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        Log.d("TimetableItem", "LessonDelegate create")
        val layoutInflater = LayoutInflater.from(parent.context)
        return LessonViewHolder(V2ItemLessonBinding.inflate(layoutInflater, parent, false))
    }

    override fun bindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: TimetableItem,
        payloads: MutableList<Any>
    ) {
        Log.d("TimetableItem", "LessonDelegate bind")
        (viewHolder as LessonViewHolder).bind(item as TimetableItem.Lesson)
    }

    inner class LessonViewHolder(private val binding: V2ItemLessonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TimetableItem.Lesson) {
            binding.timeTextView.text = item.time
            binding.lessonTextView.text = item.lessonName
        }
    }
}