package com.schedule.schedule.v2.container.adapter.model

import com.schedule.views.adapter.adaptersdelegate.CustomDiffUtilCallback

sealed interface SettingsDrawerItem {
    data class Title(val title: String) : SettingsDrawerItem
    data class PressItem(val title: String, val iconRes: Int, val onItemClick: () -> Unit) : SettingsDrawerItem

    class SettingsDrawerItemDiffUtil() : CustomDiffUtilCallback<SettingsDrawerItem> {
        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<SettingsDrawerItem>,
            newList: List<SettingsDrawerItem>
        ): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return if (oldItem is Title && newItem is Title)
                oldItem == newItem
            else if (oldItem is PressItem && newItem is PressItem)
                oldItem == newItem
            else false
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<SettingsDrawerItem>,
            newList: List<SettingsDrawerItem>
        ): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }

        override fun getChangePayload(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<SettingsDrawerItem>,
            newList: List<SettingsDrawerItem>
        ): Any? = null
    }
}