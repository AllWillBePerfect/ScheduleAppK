package com.example.models.ui

data class ScheduleGroupsListEntity(
    val choices: List<ScheduleGroupEntity>

) {
    data class ScheduleGroupEntity(
        val name: String,
        val id: String,
        val group: String
    )
}


