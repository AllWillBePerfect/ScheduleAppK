package com.schedule.network.retrofit

import com.schedule.network.retrofit.repositories.ScheduleApiSingle
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitFactory {
    private var retrofit: Retrofit? = null
    private var retrofitSingle: Retrofit? = null
    private const val url = "https://webictis.sfedu.ru/"

    private fun getClient(baseUrl: String): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    private fun getSingleClient(baseUrl: String): Retrofit {
        if (retrofitSingle == null) {
            retrofitSingle = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofitSingle!!
    }

    fun getScheduleApi(): ScheduleApi {
        return getClient(url).create(ScheduleApi::class.java)
    }

    fun getScheduleApiSingle(): ScheduleApiSingle =
        getSingleClient(url).create(ScheduleApiSingle::class.java)
}