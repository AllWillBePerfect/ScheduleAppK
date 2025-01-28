package com.schedulev2.data.repositories

import com.schedulev2.data.mappers.toEntity
import com.schedulev2.models.ui.ScheduleEntity
import com.schedulev2.models.ui.ScheduleGroupsListEntity
import com.schedulev2.network.retrofit.RetrofitFactory
import com.schedulev2.network.retrofit.models.ScheduleDTO
import com.schedulev2.network.retrofit.models.ScheduleGroupsListDTO
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

interface ScheduleApiRepository {
    fun fetchScheduleByGroupNameObservable(query: String): Observable<ScheduleEntity>
    fun fetchScheduleByHtmlObservable(query: String): Observable<ScheduleEntity>
    fun fetchScheduleByWeekObservable(
        group: String, week: String
    ): Observable<ScheduleEntity>
    fun fetchGroupListObservable(query: String): Observable<ScheduleGroupsListEntity>

    fun fetchScheduleByGroupNameSingle(query: String): Single<ScheduleEntity>
    fun fetchScheduleByHtmlSingle(query: String): Single<ScheduleEntity>
    fun fetchScheduleByWeekSingle(
        group: String, week: String
    ): Single<ScheduleEntity>
    fun fetchGroupListSingle(query: String): Single<ScheduleGroupsListEntity>

    fun fetchScheduleByGroupName(query: String): ScheduleEntity
    fun fetchScheduleByHtml(query: String): ScheduleEntity
    fun fetchScheduleByWeek(
        group: String, week: String
    ): ScheduleEntity
    fun fetchGroupList(query: String): ScheduleGroupsListEntity

    fun fetchScheduleByGroupNameDTO(query: String): Single<ScheduleDTO>
    fun fetchScheduleByHtmlDTO(query: String): Single<ScheduleDTO>
    fun fetchScheduleByWeekDTO(
        group: String, week: String
    ): Single<ScheduleDTO>
    fun fetchGroupListDTO(query: String): Single<ScheduleGroupsListDTO>

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

        override fun fetchScheduleByGroupNameSingle(query: String): Single<ScheduleEntity> =
            scheduleApi.fetchScheduleByGroupNameSingle(query).map { it.toEntity() }

        override fun fetchScheduleByHtmlSingle(query: String): Single<ScheduleEntity> =
            scheduleApi.fetchScheduleByHtmlSingle(query).map { it.toEntity() }

        override fun fetchScheduleByWeekSingle(
            group: String,
            week: String
        ): Single<ScheduleEntity> =
            scheduleApi.fetchScheduleByWeekSingle(group, week).map { it.toEntity() }

        override fun fetchGroupListSingle(query: String): Single<ScheduleGroupsListEntity> =
            scheduleApi.fetchGroupListSingle(query).map { it.toEntity() }

        override fun fetchScheduleByGroupName(query: String): ScheduleEntity =
            scheduleApiSingle.fetchScheduleByGroupName(query).toEntity()

        override fun fetchScheduleByHtml(query: String): ScheduleEntity =
            scheduleApiSingle.fetchScheduleByHtml(query).toEntity()

        override fun fetchScheduleByWeek(group: String, week: String): ScheduleEntity =
            scheduleApiSingle.fetchScheduleByWeek(group, week).toEntity()

        override fun fetchGroupList(query: String): ScheduleGroupsListEntity =
            scheduleApiSingle.fetchGroupList(query).toEntity()

        override fun fetchScheduleByGroupNameDTO(query: String): Single<ScheduleDTO> =
            scheduleApi.fetchScheduleByGroupNameSingle(query)

        override fun fetchScheduleByHtmlDTO(query: String): Single<ScheduleDTO> =
            scheduleApi.fetchScheduleByHtmlSingle(query)

        override fun fetchScheduleByWeekDTO(group: String, week: String): Single<ScheduleDTO> =
            scheduleApi.fetchScheduleByWeekSingle(group, week)

        override fun fetchGroupListDTO(query: String): Single<ScheduleGroupsListDTO> =
            scheduleApi.fetchGroupListSingle(query)

    }
}