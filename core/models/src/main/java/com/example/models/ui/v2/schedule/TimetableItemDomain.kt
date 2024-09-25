package com.example.models.ui.v2.schedule

sealed class TimetableItemDomain {
    data class Lesson(
        val time: String,
        val lessonName: String,
    ) : TimetableItemDomain()

    data class LessonCurrent(
        val time: String,
        val lessonName: String,
        val progressValue: Int
    ) : TimetableItemDomain()

    data class Break(
        val time: String,
        val lessonName: String,
        val progressValue: Int
    ) : TimetableItemDomain()

    data class Title(
        val date: String,
        val dayOfWeekName: String,
        val groupName: String,
        val isTitleEnabled: Boolean,
    ) : TimetableItemDomain()

    data class TitleCurrent(
        val date: String,
        val dayOfWeekName: String,
        val groupName: String,
        val isTitleEnabled: Boolean
    ) : TimetableItemDomain()
}
