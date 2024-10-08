package com.schedule.schedule.v2.adapter.recyclerview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.schedule.schedule.databinding.V2ItemLessonBinding
import com.schedule.schedule.v2.adapter.recyclerview.model.TimetableItem
import com.schedule.views.adapter.adaptersdelegate.AdapterItemDelegate

class LessonDelegate : AdapterItemDelegate<TimetableItem> {
    override fun forItem(item: TimetableItem): Boolean = item is TimetableItem.Lesson

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
//        Log.d("TimetableItem", "LessonDelegate create")
        val layoutInflater = LayoutInflater.from(parent.context)
        return LessonViewHolder(V2ItemLessonBinding.inflate(layoutInflater, parent, false))
    }

    override fun bindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: TimetableItem,
        payloads: MutableList<Any>
    ) {
//        Log.d("TimetableItem", "LessonDelegate bind")
        (viewHolder as LessonViewHolder).bind(item as TimetableItem.Lesson)
    }

    inner class LessonViewHolder(private val binding: V2ItemLessonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TimetableItem.Lesson) {
            binding.timeTextView.text = item.time
            binding.lessonTextView.text = item.lessonName
            when (item.lessonContentType) {
                TimetableItem.ContentType.NONE -> {
                    binding.LessonStateColor.visibility = View.GONE
                    binding.LessonStateText.visibility = View.GONE
                }

                TimetableItem.ContentType.ONLINE -> {
                    binding.LessonStateColor.visibility = View.VISIBLE
                    binding.LessonStateText.visibility = View.VISIBLE
                    binding.LessonStateColor.setImageResource(com.schedule.values.R.drawable.online)
                    binding.LessonStateText.text = "Онлайн"

                }

                TimetableItem.ContentType.OFFLINE -> {
                    binding.LessonStateColor.visibility = View.VISIBLE
                    binding.LessonStateText.visibility = View.VISIBLE
                    binding.LessonStateColor.setImageResource(com.schedule.values.R.drawable.offline)
                    binding.LessonStateText.text = "Очно"
                }
            }
        }
    }
}