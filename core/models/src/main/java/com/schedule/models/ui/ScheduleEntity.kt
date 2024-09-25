package com.schedule.models.ui


data class ScheduleEntity(
    val type: String,
    val name: String,
    val week: Int,
    val group: String,
    val table: List<List<String>>,
    val link: String,
    val weeks: List<Int>,
    val cacheTime: String,
    val cacheDate: String,
    val status: ScheduleEntityStatus
)

enum class ScheduleEntityStatus {
    CACHE, NETWORK
}


