package com.schedule.network.retrofit.repositories

import com.schedule.network.retrofit.ScheduleApi
import com.schedule.network.retrofit.models.ScheduleDTO
import com.schedule.network.retrofit.models.ScheduleGroupsListDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleApiSingle {
    @GET(ScheduleApi.GET_URI_PART)
    fun fetchScheduleByGroupName(@Query(ScheduleApi.QUERY_PARAM_QUERY) query: String?): ScheduleDTO

    @GET(ScheduleApi.GET_URI_PART)
    fun fetchScheduleByHtml(@Query(ScheduleApi.QUERY_PARAM_GROUP) query: String?): ScheduleDTO

    @GET(ScheduleApi.GET_URI_PART)
    fun fetchScheduleByWeek(
        @Query(ScheduleApi.QUERY_PARAM_GROUP) group: String?, @Query(
            ScheduleApi.QUERY_PARAM_WEEK
        ) week: String?
    ): ScheduleDTO

    @GET(ScheduleApi.GET_URI_PART)
    fun fetchGroupList(@Query(ScheduleApi.QUERY_PARAM_QUERY) query: String) : ScheduleGroupsListDTO
}