package com.schedule.schedule.v2.container

import androidx.lifecycle.ViewModel
import com.schedule.data.event_manager.LaunchWebViewEventManager
import com.schedule.data.repositories.settings.AdditionalOptionRepository
import com.schedule.schedule.v2.ScheduleFragmentContractV2
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationDrawerContainerFragmentViewModel @Inject constructor(
    private val additionalOptionRepository: AdditionalOptionRepository,
    private val launchWebViewEventManager: LaunchWebViewEventManager,
    private val routerV2: ScheduleFragmentContractV2

) : ViewModel() {

    fun getState() = additionalOptionRepository.getLaunchInWebViewState()

    fun launchWebView(url: String) {
        launchWebViewEventManager.setLaunchWebViewLiveData(url)
        routerV2.launchWebView()
    }
}