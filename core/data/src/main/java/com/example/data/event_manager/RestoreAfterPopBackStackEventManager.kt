package com.example.data.event_manager

import com.example.utils.sources.SingleEvent
import com.example.utils.sources.SingleLiveData
import com.example.utils.sources.SingleMutableLiveData
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