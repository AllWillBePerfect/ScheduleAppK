package com.schedule.network.retrofit.models


data class ScheduleDTO(
    val table: ScheduleDataDTO,
    val weeks: List<Int>
) {
    data class ScheduleDataDTO(
        val type: String,
        val name: String,
        val week: Int,
        val group: String,
        val table: List<List<String>>,
        val link: String
    )

}


