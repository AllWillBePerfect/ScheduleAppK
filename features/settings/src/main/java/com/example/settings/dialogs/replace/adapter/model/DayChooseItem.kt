package com.example.settings.dialogs.replace.adapter.model

import com.example.views.adapter.adaptersdelegate.CustomDiffUtilCallback

sealed class DayChooseItem() {
    data class ChooseDay(val day: String, val isSelected: Boolean = false) : DayChooseItem()
    data class ClickableChooseDay(val day: String, val isSelected: Boolean = false) : DayChooseItem()

    class DayChooseItemDiffUtil : CustomDiffUtilCallback<DayChooseItem> {
        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<DayChooseItem>,
            newList: List<DayChooseItem>
        ): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return if (oldItem is ChooseDay && newItem is ChooseDay)
                oldItem.day == newItem.day
            else if (oldItem is ClickableChooseDay && newItem is ClickableChooseDay)
                oldItem.day == newItem.day
            else false
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<DayChooseItem>,
            newList: List<DayChooseItem>
        ): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }

        override fun getChangePayload(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<DayChooseItem>,
            newList: List<DayChooseItem>
        ): Any? {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return null
        }
    }
}
