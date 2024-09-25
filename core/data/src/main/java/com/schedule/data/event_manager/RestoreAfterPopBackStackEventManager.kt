package com.schedule.data.event_manager

import com.schedule.utils.sources.SingleEvent
import com.schedule.utils.sources.SingleLiveData
import com.schedule.utils.sources.SingleMutableLiveData
import javax.inject.Inject

interface RestoreAfterPopBackStackEventManager {

    fun getRestoreStateAfterPopBackStackLiveData(): SingleLiveData<Boolean>
    fun setRestoreStateAfterPopBackStackLiveData()

    class Impl @Inject constructor(): RestoreAfterPopBackStackEventManager {

        private val restoreStateLiveData: SingleMutableLiveData<Boolean> = SingleMutableLiveData(
            SingleEvent(null)
        )


        override fun getRestoreStateAfterPopBackStackLiveData() = restoreStateLiveData

        override fun setRestoreStateAfterPopBackStackLiveData() {
            restoreStateLiveData.value = SingleEvent(true)
        }
    }
}