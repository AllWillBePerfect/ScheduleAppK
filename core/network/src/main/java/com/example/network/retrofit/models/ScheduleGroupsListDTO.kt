package com.example.network.retrofit.models


data class ScheduleGroupsListDTO(
    val choices: List<ScheduleGroupDTO>
) {
    data class ScheduleGroupDTO(
        val name: String,
        val id: String,
        val group: String
    )
}

