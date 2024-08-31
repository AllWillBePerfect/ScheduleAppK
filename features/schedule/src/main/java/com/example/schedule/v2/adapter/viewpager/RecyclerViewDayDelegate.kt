package com.example.schedule.v2.adapter.viewpager

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.schedule.databinding.V2ItemRecyclerViewDayBinding
import com.example.schedule.v2.adapter.recyclerview.BreakDelegate
import com.example.schedule.v2.adapter.recyclerview.LessonCurrentDelegate
import com.example.schedule.v2.adapter.recyclerview.LessonDelegate
import com.example.schedule.v2.adapter.recyclerview.TitleCurrentDelegate
import com.example.schedule.v2.adapter.recyclerview.TitleDelegate
import com.example.schedule.v2.adapter.recyclerview.model.TimetableItem
import com.example.schedule.v2.adapter.viewpager.model.ViewPagerItem
import com.example.views.adapter.adaptersdelegate.AdapterItemDelegate
import com.example.views.adapter.adaptersdelegate.UniversalRecyclerViewAdapter

class RecyclerViewDayDelegate : AdapterItemDelegate<ViewPagerItem> {
    override fun forItem(item: ViewPagerItem): Boolean = item is ViewPagerItem.RecyclerViewDay

    override fun getViewHolder(parent: ViewGroup): ViewHolder {
        Log.d("ViewPagerItem", "RecyclerViewDayDelegate create")
        val layoutInflater = LayoutInflater.from(parent.context)
        return RecyclerViewDayViewHolder(
            V2ItemRecyclerViewDayBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun bindViewHolder(
        viewHolder: ViewHolder,
        item: ViewPagerItem,
        payloads: MutableList<Any>
    ) {
        Log.d("ViewPagerItem", "RecyclerViewDayDelegate bind")
        (viewHolder as RecyclerViewDayViewHolder).bind(item as ViewPagerItem.RecyclerViewDay)
    }


    inner class RecyclerViewDayViewHolder(private val binding: V2ItemRecyclerViewDayBinding) :
        ViewHolder(binding.root) {
        var adapter: UniversalRecyclerViewAdapter<TimetableItem> = UniversalRecyclerViewAdapter(
            delegates = listOf(
                LessonDelegate(),
                LessonCurrentDelegate(),
                TitleDelegate(),
                TitleCurrentDelegate(),
                BreakDelegate()
            ),
            diffUtilCallback = TimetableItem.TimetableItemDiffUtil()
        )

        init {
            binding.recyclerView.adapter = adapter
        }

        fun bind(item: ViewPagerItem.RecyclerViewDay) {
            adapter.items = item.lessons
        }

    }
}