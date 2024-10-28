package com.schedulev2.data.event_manager

import com.schedulev2.utils.sources.SingleEvent
import com.schedulev2.utils.sources.SingleLiveData
import com.schedulev2.utils.sources.SingleMutableLiveData
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