package com.example.utils

sealed class ScheduleItem(val id: String) {

    data class OtherDay(
        val dayOfWeekName: String,
        val date: String,
        val lessonsList: List<String>,
        ) : ScheduleItem(date)

    data class CurrentDay(
        val dayOfWeekName: String,
        val date: String,
        val lessonsList: List<String>,
        val lessonProgress: LessonProgress
    ) :
        ScheduleItem(date)

    data class Empty(
        val text: String
    ) : ScheduleItem(text)
}