package com.schedulev2.schedule.v2.adapter.viewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.schedulev2.schedule.databinding.V2ItemRecyclerViewDayBinding
import com.schedulev2.schedule.v2.adapter.recyclerview.BreakDelegate
import com.schedulev2.schedule.v2.adapter.recyclerview.LessonCurrentDelegate
import com.schedulev2.schedule.v2.adapter.recyclerview.LessonDelegate
import com.schedulev2.schedule.v2.adapter.recyclerview.TitleCurrentDelegate
import com.schedulev2.schedule.v2.adapter.recyclerview.TitleDelegate
import com.schedulev2.schedule.v2.adapter.recyclerview.model.TimetableItem
import com.schedulev2.schedule.v2.adapter.viewpager.model.ViewPagerItem
import com.schedulev2.views.adapter.adaptersdelegate.AdapterItemDelegate
import com.schedulev2.views.adapter.adaptersdelegate.UniversalRecyclerViewAdapter

class RecyclerViewDayCurrentDelegate(
    private val onItemClickListener: (() -> Unit)
) : AdapterItemDelegate<ViewPagerItem> {
    override fun forItem(item: ViewPagerItem): Boolean =
        item is ViewPagerItem.RecyclerViewCurrentDay

    override fun getViewHolder(parent: ViewGroup): ViewHolder {
//        Log.d("ViewPagerItem", "RecyclerViewDayCurrentDelegate create")
        val layoutInflater = LayoutInflater.from(parent.context)
        return RecyclerViewDayCurrentViewHolder(
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
//        Log.d("ViewPagerItem", "RecyclerViewDayCurrentDelegate bind")
        (viewHolder as RecyclerViewDayCurrentViewHolder).bind(item as ViewPagerItem.RecyclerViewCurrentDay)
    }


    class RecyclerViewDayCurrentViewHolder(val binding: V2ItemRecyclerViewDayBinding) :
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
            binding.recyclerView.addOnItemTouchListener(RecyclerViewDayDelegate.RecyclerViewDayViewHolder.CustomOnItemTouchListener())

        }

        fun bind(item: ViewPagerItem.RecyclerViewCurrentDay) {
            adapter.items = item.lessons
        }

        fun scrollToPosition(position: Int) {
            binding.recyclerView.scrollToPosition(position)
        }


    }

}