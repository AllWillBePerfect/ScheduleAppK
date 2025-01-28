package com.schedulev2.network.retrofit

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

    fun getScheduleApi(): ScheduleApiRx {
        return getClient(url).create(ScheduleApiRx::class.java)
    }

    fun getScheduleApiSingle(): ScheduleApi =
        getSingleClient(url).create(ScheduleApi::class.java)
}