package com.example.sharpref.sources

import com.example.models.sharpref.NightMode
import com.example.sharpref.configs.SHCNightMode
import javax.inject.Inject

interface NightModeContract {
    fun getNightMode(): NightMode
    fun setNightMode(newNightMode: NightMode)

    class Impl @Inject constructor(private val shcNightMode: SHCNightMode) : NightModeContract {
        override fun getNightMode(): NightMode = shcNightMode.getValue()

        override fun setNightMode(newNightMode: NightMode) = shcNightMode.setValue(newNightMode)
    }
}