package com.schedule.settings

import androidx.lifecycle.ViewModel
import com.schedule.data.repositories.SettingsOptionRepository
import com.schedule.data.repositories.settings.DynamicColorsRepository
import com.schedule.data.repositories.v2.schedule.repository.impl.AppConfigRepositoryV2
import com.schedule.data.event_manager.RefreshEventManager
import com.schedule.data.event_manager.RestoreDialogEventManager
import com.schedule.data.repositories.settings.AdditionalOptionRepository
import com.schedule.models.sharpref.NightMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsOptionRepository: SettingsOptionRepository,
    private val dynamicColorsRepository: DynamicColorsRepository,
    private val appConfigRepositoryV2: AppConfigRepositoryV2,
    private val additionalOptionRepository: AdditionalOptionRepository,
    private val refreshEventManager: RefreshEventManager,
    private val restoreDialogEventManager: RestoreDialogEventManager
) : ViewModel() {

    val updateUiLiveData = refreshEventManager.getRefreshLiveData()
    val restoreDialogLiveData = restoreDialogEventManager.getRestoreDialogLiveData()

    fun getNightMode(): Int {
        return when (settingsOptionRepository.getNightMode()) {
            NightMode.NO -> 0
            NightMode.YES -> 1
            NightMode.FOLLOW_SYSTEM -> 2
        }
    }

    fun setNightMode(nightMode: Int) {
        when (nightMode) {
            0 -> settingsOptionRepository.setLightMode()
            1 -> settingsOptionRepository.setNightMode()
            2 -> settingsOptionRepository.setFollowSystemMode()
        }
    }



    fun getDynamicColors() = dynamicColorsRepository.getDynamicColorsState()
    fun changeDynamicColors() = dynamicColorsRepository.changeDynamicColors()

    fun singleGroupStateCheck(): Boolean = appConfigRepositoryV2.getSingleAppStateOrNull() != null
    fun replaceGroupStateCheck(): Boolean = appConfigRepositoryV2.getReplaceAppStateOrNull() != null
    fun multipleGroupStateCheck(): Boolean = appConfigRepositoryV2.getMultipleAppStateOrNull() != null

    fun getMultipleGroupTabLayoutState(): Boolean = additionalOptionRepository.getMultipleGroupFastScrollState()
    fun switchMultipleGroupTabLayoutState() {
        additionalOptionRepository.switchMultipleGroupFastScrollState()
        refreshEventManager.setRefreshLiveData()
    }

    fun getLaunchInWebView(): Boolean = additionalOptionRepository.getLaunchInWebViewState()
    fun switchLaunchInWebView() {
        additionalOptionRepository.switchLaunchInWebViewState()
        refreshEventManager.setRefreshLiveData()
    }


}