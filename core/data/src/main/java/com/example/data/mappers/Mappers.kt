package com.example.data.mappers

import com.example.models.ui.ScheduleEntity
import com.example.models.ui.ScheduleGroupsListEntity
import com.example.network.retrofit.models.ScheduleDTO
import com.example.network.retrofit.models.ScheduleGroupsListDTO

fun ScheduleDTO.toEntity() = ScheduleEntity(
    type = table.type,
    name = table.name,
    week = table.week,
    group = table.group,
    table = table.table,
    link = table.link,
    weeks = weeks
)

fun ScheduleGroupsListDTO.toEntity(): ScheduleGroupsListEntity = ScheduleGroupsListEntity(
    choices = choices.map { ScheduleGroupsListEntity.ScheduleGroupEntity(
        name = it.name,
        id = it.id,
        group = it.group
    ) }
)