package com.schedulev2.sharpref.sources

import com.schedulev2.models.sharpref.NightMode
import com.schedulev2.sharpref.configs.SHCNightMode
import javax.inject.Inject

interface NightModeContract {
    fun getNightMode(): NightMode
    fun setNightMode(newNightMode: NightMode)

    class Impl @Inject constructor(private val shcNightMode: SHCNightMode) : NightModeContract {
        override fun getNightMode(): NightMode = shcNightMode.getValue()

        override fun setNightMode(newNightMode: NightMode) = shcNightMode.setValue(newNightMode)
    }
}