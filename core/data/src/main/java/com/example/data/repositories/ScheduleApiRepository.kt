package com.example.data.repositories

import com.example.data.mappers.toEntity
import com.example.models.ui.ScheduleEntity
import com.example.models.ui.ScheduleGroupsListEntity
import com.example.network.retrofit.RetrofitFactory
import io.reactivex.Observable
import javax.inject.Inject

interface ScheduleApiRepository {
    fun fetchScheduleByGroupNameObservable(query: String): Observable<ScheduleEntity>
    fun fetchScheduleByHtmlObservable(query: String): Observable<ScheduleEntity>
    fun fetchScheduleByWeekObservable(
        group: String, week: String
    ): Observable<ScheduleEntity>
    fun fetchGroupListObservable(query: String): Observable<ScheduleGroupsListEntity>

    fun fetchScheduleByGroupName(query: String): ScheduleEntity
    fun fetchScheduleByHtml(query: String): ScheduleEntity
    fun fetchScheduleByWeek(
        group: String, week: String
    ): ScheduleEntity
    fun fetchGroupList(query: String): ScheduleGroupsListEntity

    class Impl @Inject constructor() :
        ScheduleApiRepository {
        val scheduleApi = RetrofitFactory.getScheduleApi()
        val scheduleApiSingle = RetrofitFactory.getScheduleApiSingle()

        override fun fetchScheduleByGroupNameObservable(query: String): Observable<ScheduleEntity> =
            scheduleApi.fetchScheduleByGroupNameObservable(query).map { it.toEntity() }

        override fun fetchScheduleByHtmlObservable(query: String): Observable<ScheduleEntity> =
            scheduleApi.fetchScheduleByHtmlObservable(query).map { it.toEntity() }

        override fun fetchScheduleByWeekObservable(
            group: String,
            week: String
        ): Observable<ScheduleEntity> =
            scheduleApi.fetchScheduleByWeekObservable(group, week).map { it.toEntity() }

        override fun fetchGroupListObservable(query: String): Observable<ScheduleGroupsListEntity> =
            scheduleApi.fetchGroupListObservable(query).map { it.toEntity() }

        override fun fetchScheduleByGroupName(query: String): ScheduleEntity =
            scheduleApiSingle.fetchScheduleByGroupName(query).toEntity()

        override fun fetchScheduleByHtml(query: String): ScheduleEntity =
            scheduleApiSingle.fetchScheduleByHtml(query).toEntity()

        override fun fetchScheduleByWeek(group: String, week: String): ScheduleEntity =
            scheduleApiSingle.fetchScheduleByWeek(group, week).toEntity()

        override fun fetchGroupList(query: String): ScheduleGroupsListEntity =
            scheduleApiSingle.fetchGroupList(query).toEntity()

    }
}