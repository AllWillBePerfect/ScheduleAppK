package com.schedule.sharpref.sources

import com.schedule.models.sharpref.DynamicColors
import com.schedule.sharpref.configs.SHCDynamicColors
import javax.inject.Inject

interface DynamicColorsContract {
    fun getDynamicColors(): DynamicColors
    fun setDynamicColors(dynamicColors: DynamicColors)

    class Impl @Inject constructor(private val config: SHCDynamicColors) : DynamicColorsContract {
        override fun getDynamicColors(): DynamicColors = config.getValue()
        override fun setDynamicColors(dynamicColors: DynamicColors) = config.setValue(dynamicColors)
    }
}