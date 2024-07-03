package com.example.data.service

import com.example.utils.sources.SingleEvent
import com.example.utils.sources.SingleLiveData
import com.example.utils.sources.SingleMutableLiveData
import javax.inject.Inject

interface RefreshService {

    fun getRefreshLiveData(): SingleLiveData<Boolean>
    fun setRefreshLiveData()

    class Impl @Inject constructor() : RefreshService {

        private val refreshLiveData: SingleMutableLiveData<Boolean> = SingleMutableLiveData(SingleEvent(null))

        override fun getRefreshLiveData(): SingleLiveData<Boolean> = refreshLiveData

        override fun setRefreshLiveData() {
            refreshLiveData.value = SingleEvent(true)
        }
    }
}