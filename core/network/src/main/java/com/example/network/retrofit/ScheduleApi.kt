package com.example.network.retrofit

import com.example.network.retrofit.models.ScheduleDTO
import com.example.network.retrofit.models.ScheduleGroupsListDTO
import io.reactivex.Observable
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



    companion object {
        const val GET_URI_PART = "schedule-api/"
        const val QUERY_PARAM_QUERY = "query"
        const val QUERY_PARAM_GROUP = "group"
        const val QUERY_PARAM_WEEK = "week"
    }
}

