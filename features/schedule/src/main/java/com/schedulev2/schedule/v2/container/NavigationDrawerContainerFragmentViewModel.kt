package com.schedulev2.schedule.v2.container

import androidx.lifecycle.ViewModel
import com.schedulev2.data.event_manager.LaunchWebViewEventManager
import com.schedulev2.data.repositories.settings.AdditionalOptionRepository
import com.schedulev2.schedule.v2.ScheduleFragmentContractV2
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