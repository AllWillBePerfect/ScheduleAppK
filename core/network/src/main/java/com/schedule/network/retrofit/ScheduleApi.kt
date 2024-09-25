package com.schedule.network.retrofit

import com.schedule.network.retrofit.models.ScheduleDTO
import com.schedule.network.retrofit.models.ScheduleGroupsListDTO
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleApi {

    @GET(GET_URI_PART)
    fun fetchScheduleByGroupNameObservable(@Query(QUERY_PARAM_QUERY) query: String?): Observable<ScheduleDTO>

    @GET(GET_URI_PART)
    fun fetchScheduleByHtmlObservable(@Query(QUERY_PARAM_GROUP) query: String?): Observable<ScheduleDTO>

    @GET(GET_URI_PART)
    fun fetchScheduleByWeekObservable(
        @Query(QUERY_PARAM_GROUP) group: String?, @Query(
            QUERY_PARAM_WEEK
        ) week: String?
    ): Observable<ScheduleDTO>

    @GET(GET_URI_PART)
    fun fetchGroupListObservable(@Query(QUERY_PARAM_QUERY) query: String) : Observable<ScheduleGroupsListDTO>

    @GET(GET_URI_PART)
    fun fetchScheduleByGroupNameSingle(@Query(QUERY_PARAM_QUERY) query: String?): Single<ScheduleDTO>

    @GET(GET_URI_PART)
    fun fetchScheduleByHtmlSingle(@Query(QUERY_PARAM_GROUP) query: String?): Single<ScheduleDTO>

    @GET(GET_URI_PART)
    fun fetchScheduleByWeekSingle(
        @Query(QUERY_PARAM_GROUP) group: String?, @Query(
            QUERY_PARAM_WEEK
        ) week: String?
    ): Single<ScheduleDTO>

    @GET(GET_URI_PART)
    fun fetchGroupListSingle(@Query(QUERY_PARAM_QUERY) query: String) : Single<ScheduleGroupsListDTO>



    companion object {
        const val GET_URI_PART = "schedule-api/"
        const val QUERY_PARAM_QUERY = "query"
        const val QUERY_PARAM_GROUP = "group"
        const val QUERY_PARAM_WEEK = "week"
    }
}

