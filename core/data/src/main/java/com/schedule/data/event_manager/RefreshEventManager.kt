package com.schedule.data.event_manager

import com.schedule.data.repositories.v2.schedule.repository.impl.AppConfigRepositoryV2
import com.schedule.models.sharpref.v2.AppStateV2
import com.schedule.utils.sources.SingleEvent
import com.schedule.utils.sources.SingleLiveData
import com.schedule.utils.sources.SingleMutableLiveData
import javax.inject.Inject

interface RefreshEventManager {

    fun getRefreshLiveData(): SingleLiveData<Boolean>
    fun setRefreshLiveData()

    class Impl @Inject constructor(
        private val appConfigRepositoryV2: AppConfigRepositoryV2,
        ) : RefreshEventManager {

        private val refreshLiveData: SingleMutableLiveData<Boolean> = SingleMutableLiveData(SingleEvent(null))

        override fun getRefreshLiveData(): SingleLiveData<Boolean> = refreshLiveData

        override fun setRefreshLiveData() {
            val state = appConfigRepositoryV2.getAppState()
            when (state) {
                is AppStateV2.Single -> {}
                is AppStateV2.Multiple -> {appConfigRepositoryV2.selectMultipleGroupAndSetState()}
                is AppStateV2.Replace -> {}
                is AppStateV2.Unselected -> {}
            }
            refreshLiveData.value = SingleEvent(true)
        }
    }
}