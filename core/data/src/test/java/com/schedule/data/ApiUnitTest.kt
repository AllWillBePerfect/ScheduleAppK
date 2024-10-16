package com.schedule.data

import com.google.gson.Gson
import com.schedule.network.retrofit.RetrofitFactory
import com.schedule.network.retrofit.ScheduleApiRx
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

class ApiUnitTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var scheduleApi: ScheduleApiRx

    @Before
    fun startUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        scheduleApi = RetrofitFactory.getScheduleApi()

        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }

    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        RxJavaPlugins.reset()
    }

    @Test
    fun `test successful data fetch`() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
//            .setBody("{\"result\": \"success\"}")
        mockWebServer.enqueue(mockResponse)

        val testObserver = scheduleApi.fetchScheduleByGroupNameSingle("КТбо4-2").test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue { it.table.name == "КТбо4-2" }
    }

    @Test
    fun `test no entry fetch`() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        val testObserver = scheduleApi.fetchScheduleByGroupNameSingleResponse("КТбо4-222").test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue {
            val jsonString = it.body()?.string()
            val gson = Gson()
            val testNoEntries = gson.fromJson(jsonString, TestNoEntries::class.java)
            testNoEntries.result == "no_entries"
        }


    }

    data class TestNoEntries(val result: String)

}