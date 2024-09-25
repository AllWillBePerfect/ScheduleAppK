package com.schedule.data.event_manager

import com.schedule.utils.sources.SingleEvent
import com.schedule.utils.sources.SingleLiveData
import com.schedule.utils.sources.SingleMutableLiveData
import javax.inject.Inject

interface RefreshEventManager {

    fun getRefreshLiveData(): SingleLiveData<Boolean>
    fun setRefreshLiveData()

    class Impl @Inject constructor() : RefreshEventManager {

        private val refreshLiveData: SingleMutableLiveData<Boolean> = SingleMutableLiveData(SingleEvent(null))

        override fun getRefreshLiveData(): SingleLiveData<Boolean> = refreshLiveData

        override fun setRefreshLiveData() {
            refreshLiveData.value = SingleEvent(true)
        }
    }
}