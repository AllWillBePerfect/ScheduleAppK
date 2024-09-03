package com.example.settings

import androidx.lifecycle.ViewModel
import com.example.data.repositories.SettingsOptionRepository
import com.example.data.repositories.settings.DynamicColorsRepository
import com.example.models.sharpref.NightMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsOptionRepository: SettingsOptionRepository,
    private val dynamicColorsRepository: DynamicColorsRepository
) : ViewModel() {

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
}