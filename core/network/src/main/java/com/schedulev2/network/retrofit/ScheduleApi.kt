package com.schedulev2.network.retrofit

import com.schedulev2.network.retrofit.models.ScheduleDTO
import com.schedulev2.network.retrofit.models.ScheduleGroupsListDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleApi {
    @GET(ScheduleApiRx.GET_URI_PART)
    fun fetchScheduleByGroupName(@Query(ScheduleApiRx.QUERY_PARAM_QUERY) query: String?): ScheduleDTO

    @GET(ScheduleApiRx.GET_URI_PART)
    fun fetchScheduleByHtml(@Query(ScheduleApiRx.QUERY_PARAM_GROUP) query: String?): ScheduleDTO

    @GET(ScheduleApiRx.GET_URI_PART)
    fun fetchScheduleByWeek(
        @Query(ScheduleApiRx.QUERY_PARAM_GROUP) group: String?, @Query(
            ScheduleApiRx.QUERY_PARAM_WEEK
        ) week: String?
    ): ScheduleDTO

    @GET(ScheduleApiRx.GET_URI_PART)
    fun fetchGroupList(@Query(ScheduleApiRx.QUERY_PARAM_QUERY) query: String) : ScheduleGroupsListDTO
}