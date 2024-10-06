package com.schedule.schedule.v2.adapter.recyclerview


import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.schedule.schedule.databinding.V2ItemLessonCurrentBinding
import com.schedule.schedule.v2.adapter.recyclerview.model.TimetableItem
import com.schedule.views.adapter.adaptersdelegate.AdapterItemDelegate

class LessonCurrentDelegate(
): AdapterItemDelegate<TimetableItem>{
//
//    override fun onClick(p0: View?) {
//        if (p0?.id == com.schedule.schedule.R.id.v2_item_lesson_current)
//            onItemClickListener.invoke(p0.tag as TimetableItem)
//    }

    override fun forItem(item: TimetableItem): Boolean = item is TimetableItem.LessonCurrent

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
//        Log.d("TimetableItem", "LessonCurrentDelegate create")
        val binding = V2ItemLessonCurrentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LessonCurrentViewHolder(binding)
    }

    override fun bindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: TimetableItem,
        payloads: MutableList<Any>
    ) {
//        Log.d("TimetableItem", "LessonCurrentDelegate bind")
        (viewHolder as LessonCurrentViewHolder).bind(item as TimetableItem.LessonCurrent)
    }

    inner class LessonCurrentViewHolder(private val binding: V2ItemLessonCurrentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TimetableItem.LessonCurrent) {
            binding.timeTextView.text = item.time
            binding.lessonTextView.text = item.lessonName
            binding.progressIndicator.progress = item.progressValue
            binding.root.tag = item
            when (item.lessonContentType) {
                TimetableItem.ContentType.NONE -> {
                    binding.LessonStateColor.visibility = View.GONE
                    binding.LessonStateText.visibility = View.GONE
                }

                TimetableItem.ContentType.ONLINE -> {
                    binding.LessonStateColor.visibility = View.VISIBLE
                    binding.LessonStateText.visibility = View.VISIBLE
                    binding.LessonStateColor.setImageResource(com.schedule.schedule.R.drawable.online)
                    binding.LessonStateText.text = "Онлайн"

                }

                TimetableItem.ContentType.OFFLINE -> {
                    binding.LessonStateColor.visibility = View.VISIBLE
                    binding.LessonStateText.visibility = View.VISIBLE
                    binding.LessonStateColor.setImageResource(com.schedule.schedule.R.drawable.offline)
                    binding.LessonStateText.text = "Очно"
                }
            }
        }
    }


}
