package com.schedule.data.mappers

import com.schedule.database.entities.ScheduleDbo
import com.schedule.models.ui.ScheduleEntity
import com.schedule.models.ui.ScheduleEntityStatus
import com.schedule.models.ui.ScheduleGroupsListEntity
import com.schedule.network.retrofit.models.ScheduleDTO
import com.schedule.network.retrofit.models.ScheduleGroupsListDTO
import com.schedule.utils.sources.DateUtils

fun ScheduleDTO.toEntity(status: ScheduleEntityStatus = ScheduleEntityStatus.CACHE) = ScheduleEntity(
    type = table.type,
    name = table.name,
    week = table.week,
    group = table.group,
    table = table.table,
    link = table.link,
    weeks = weeks,
    cacheTime = DateUtils.getCurrentTime(),
    cacheDate = DateUtils.getCurrentDate(),
    status = status
)

fun ScheduleGroupsListDTO.toEntity(): ScheduleGroupsListEntity = ScheduleGroupsListEntity(
    choices = choices.map { ScheduleGroupsListEntity.ScheduleGroupEntity(
        name = it.name,
        id = it.id,
        group = it.group
    ) }
)

fun ScheduleEntity.toDbo(compareTable: Boolean = false): ScheduleDbo = ScheduleDbo(
    type = this.type,
    name = this.name,
    week = this.week,
    group = this.group,
    table = this.table,
    link = this.link,
    weeks = this.weeks,
    cacheTime = DateUtils.getCurrentTime(),
    cacheDate = DateUtils.getCurrentDate(),
    isCompareTable = compareTable
)

fun ScheduleDbo.toEntity(): ScheduleEntity = ScheduleEntity(
    type = this.type,
    name = this.name,
    week = this.week,
    group = this.group,
    table = this.table,
    link = this.link,
    weeks = this.weeks,
    cacheTime = this.cacheTime,
    cacheDate = this.cacheDate,
    status = ScheduleEntityStatus.CACHE
)