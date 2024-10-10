package com.schedule.data.event_manager

import com.schedule.utils.sources.SingleEvent
import com.schedule.utils.sources.SingleLiveData
import com.schedule.utils.sources.SingleMutableLiveData
import javax.inject.Inject

interface LaunchWebViewEventManager {

    fun getLaunchWebViewLiveData(): SingleLiveData<String>
    fun setLaunchWebViewLiveData(url: String)

    class Impl @Inject constructor() : LaunchWebViewEventManager {

        private val launchWebViewLiveData: SingleMutableLiveData<String> = SingleMutableLiveData(
            SingleEvent(null)
        )

        override fun getLaunchWebViewLiveData(): SingleLiveData<String> = launchWebViewLiveData

        override fun setLaunchWebViewLiveData(url: String) {
            launchWebViewLiveData.value = SingleEvent(url)
        }
    }
}