package com.example.schedule.v2.adapter.recyclerview.model

import com.example.views.adapter.adaptersdelegate.CustomDiffUtilCallback

sealed class TimetableItem {
    data class Lesson(
        val time: String,
        val lessonName: String,
    ) : TimetableItem()

    data class LessonCurrent(
        val time: String,
        val lessonName: String,
        val progressValue: Int
    ) : TimetableItem()

    data class Break(
        val time: String,
        val lessonName: String,
        val progressValue: Int
    ) : TimetableItem()

    data class Title(
        val date: String,
        val dayOfWeekName: String,
        val groupName: String,
        val isTitleEnabled: Boolean
    ) : TimetableItem()

    data class TitleCurrent(
        val date: String,
        val dayOfWeekName: String,
        val groupName: String,
        val isTitleEnabled: Boolean
    ) : TimetableItem()


    class TimetableItemDiffUtil() : CustomDiffUtilCallback<TimetableItem> {
        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<TimetableItem>,
            newList: List<TimetableItem>
        ): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return if (oldItem is Lesson && newItem is Lesson)
                oldItem == newItem
            else if (oldItem is LessonCurrent && newItem is LessonCurrent)
                oldItem == newItem
            else if (oldItem is Break && newItem is Break)
                oldItem == newItem
            else if (oldItem is Title && newItem is Title)
                oldItem == newItem
            else if (oldItem is TitleCurrent && newItem is TitleCurrent)
                oldItem == newItem
            else false

        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<TimetableItem>,
            newList: List<TimetableItem>
        ): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }

        override fun getChangePayload(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<TimetableItem>,
            newList: List<TimetableItem>
        ): Any? = null
    }
}