package com.example.schedule.v2.adapter.recyclerview


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.databinding.V2ItemLessonCurrentBinding
import com.example.schedule.v2.adapter.recyclerview.model.TimetableItem
import com.example.views.adapter.adaptersdelegate.AdapterItemDelegate

class LessonCurrentDelegate: AdapterItemDelegate<TimetableItem> {
    override fun forItem(item: TimetableItem): Boolean = item is TimetableItem.LessonCurrent

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        Log.d("TimetableItem", "LessonCurrentDelegate create")
        val layoutInflater = LayoutInflater.from(parent.context)
        return LessonCurrentViewHolder(V2ItemLessonCurrentBinding.inflate(layoutInflater, parent, false))
    }

    override fun bindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: TimetableItem,
        payloads: MutableList<Any>
    ) {
        Log.d("TimetableItem", "LessonCurrentDelegate bind")
        (viewHolder as LessonCurrentViewHolder).bind(item as TimetableItem.LessonCurrent)
    }

    inner class LessonCurrentViewHolder(private val binding: V2ItemLessonCurrentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TimetableItem.LessonCurrent) {
            binding.timeTextView.text = item.time
            binding.lessonTextView.text = item.lessonName
            binding.progressIndicator.progress = item.progressValue
        }
    }
}
