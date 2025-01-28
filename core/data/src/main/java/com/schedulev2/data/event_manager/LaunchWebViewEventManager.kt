package com.schedulev2.data.event_manager

import com.schedulev2.utils.sources.SingleEvent
import com.schedulev2.utils.sources.SingleLiveData
import com.schedulev2.utils.sources.SingleMutableLiveData
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