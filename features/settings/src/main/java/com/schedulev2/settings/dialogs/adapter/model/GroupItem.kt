package com.schedulev2.settings.dialogs.adapter.model

import com.schedulev2.views.adapter.adaptersdelegate.CustomDiffUtilCallback

sealed class GroupItem {
    data class Single(val groupName: String, val isSelected: Boolean = false) : GroupItem()
    data class Replace(
        val groupName: String,
        val vpkName: String,
        val replacedDays: List<Int>,
        val isSelected: Boolean = false,
        val isShowDays: Boolean = true
    ) : GroupItem()

    data class Multiple(val groupName: String, val isSelected: Boolean = false) : GroupItem()

    class GroupItemDiffUtil : CustomDiffUtilCallback<GroupItem> {
        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<GroupItem>,
            newList: List<GroupItem>
        ): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return if (oldItem is Single && newItem is Single)
                oldItem.groupName == newItem.groupName
            else if (oldItem is Replace && newItem is Replace)
                oldItem.groupName == newItem.groupName && oldItem.vpkName == newItem.vpkName && oldItem.replacedDays == newItem.replacedDays
            else if (oldItem is Multiple && newItem is Multiple)
                oldItem.groupName == newItem.groupName
            else false
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<GroupItem>,
            newList: List<GroupItem>
        ): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }

        override fun getChangePayload(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<GroupItem>,
            newList: List<GroupItem>
        ): Any? {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return null
        }
    }
}