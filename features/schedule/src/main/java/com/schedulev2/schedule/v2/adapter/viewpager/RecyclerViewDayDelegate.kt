package com.schedulev2.schedule.v2.adapter.viewpager

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
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

class RecyclerViewDayDelegate : AdapterItemDelegate<ViewPagerItem> {
    override fun forItem(item: ViewPagerItem): Boolean = item is ViewPagerItem.RecyclerViewDay

    override fun getViewHolder(parent: ViewGroup): ViewHolder {
//        Log.d("ViewPagerItem", "RecyclerViewDayDelegate create")
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
//        Log.d("ViewPagerItem", "RecyclerViewDayDelegate bind")
        (viewHolder as RecyclerViewDayViewHolder).bind(item as ViewPagerItem.RecyclerViewDay)
    }


    class RecyclerViewDayViewHolder(val binding: V2ItemRecyclerViewDayBinding) :
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
            binding.recyclerView.addOnItemTouchListener(CustomOnItemTouchListener())
        }

        fun bind(item: ViewPagerItem.RecyclerViewDay) {
            adapter.items = item.lessons
        }

        class CustomOnItemTouchListener() : OnItemTouchListener {

            private var startX = 0f
            private var startY = 0f

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = e.x
                        startY = e.y
                        rv.parent.requestDisallowInterceptTouchEvent(true)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val dx = e.x - startX
                        val dy = e.y - startY

                        if (Math.abs(dy) > Math.abs(dx)) {
                            // Вертикальная прокрутка: запрещаем ViewPager2 перехватывать касания
                            rv.parent.requestDisallowInterceptTouchEvent(true)
                        } else {
                            // Горизонтальная прокрутка: разрешаем ViewPager2 перехватывать касания
                            rv.parent.requestDisallowInterceptTouchEvent(false)
                        }
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                // Не требуется дополнительной обработки
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                // Не требуется дополнительной обработки
            }
        }

    }
}