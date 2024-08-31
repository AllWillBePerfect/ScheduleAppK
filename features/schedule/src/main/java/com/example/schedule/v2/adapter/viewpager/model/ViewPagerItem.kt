package com.example.schedule.v2.adapter.viewpager.model

import com.example.schedule.v2.adapter.recyclerview.model.TimetableItem
import com.example.views.adapter.adaptersdelegate.CustomDiffUtilCallback

sealed class ViewPagerItem {
    data class RecyclerViewDay(val lessons: List<TimetableItem>) : ViewPagerItem()
    data class RecyclerViewCurrentDay(val lessons: List<TimetableItem>) : ViewPagerItem()

    class ViewPagerItemDiffUtil() : CustomDiffUtilCallback<ViewPagerItem> {
        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<ViewPagerItem>,
            newList: List<ViewPagerItem>
        ): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return if (oldItem is RecyclerViewDay && newItem is RecyclerViewDay)
                oldItem == newItem
            else if (oldItem is RecyclerViewCurrentDay && newItem is RecyclerViewCurrentDay)
                oldItem == newItem
            else false

        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<ViewPagerItem>,
            newList: List<ViewPagerItem>
        ): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }

        override fun getChangePayload(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<ViewPagerItem>,
            newList: List<ViewPagerItem>
        ): Any? = null
    }
}
