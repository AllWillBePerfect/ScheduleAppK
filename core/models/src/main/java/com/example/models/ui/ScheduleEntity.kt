package com.example.models.ui


data class ScheduleEntity(
    val type: String,
    val name: String,
    val week: Int,
    val group: String,
    val table: List<List<String>>,
    val link: String,
    val weeks: List<Int>
)


