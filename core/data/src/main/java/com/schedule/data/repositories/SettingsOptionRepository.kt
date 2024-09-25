package com.schedule.data.repositories

import com.schedule.models.sharpref.NightMode
import com.schedule.sharpref.sources.NightModeContract
import javax.inject.Inject
import javax.inject.Singleton

interface SettingsOptionRepository {

    fun setLightMode()
    fun setNightMode()
    fun setFollowSystemMode()
    fun getNightMode(): NightMode
    fun getNightModeInt(): Int

    @Singleton
    class Impl @Inject constructor(
        private val nightModeContract: NightModeContract
    ) : SettingsOptionRepository {
        override fun setLightMode() {
            nightModeContract.setNightMode(NightMode.NO)
        }

        override fun setNightMode() {
            nightModeContract.setNightMode(NightMode.YES)
        }

        override fun setFollowSystemMode() {
            nightModeContract.setNightMode(NightMode.FOLLOW_SYSTEM)
        }

        override fun getNightMode(): NightMode = nightModeContract.getNightMode()
        override fun getNightModeInt(): Int = when (getNightMode()) {
            NightMode.NO -> 0
            NightMode.YES -> 1
            NightMode.FOLLOW_SYSTEM -> 2
        }
    }
}