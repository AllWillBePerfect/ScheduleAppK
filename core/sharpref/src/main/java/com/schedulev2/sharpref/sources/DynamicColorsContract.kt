package com.schedulev2.sharpref.sources

import com.schedulev2.models.sharpref.DynamicColors
import com.schedulev2.sharpref.configs.SHCDynamicColors
import javax.inject.Inject

interface DynamicColorsContract {
    fun getDynamicColors(): DynamicColors
    fun setDynamicColors(dynamicColors: DynamicColors)

    class Impl @Inject constructor(private val config: SHCDynamicColors) : DynamicColorsContract {
        override fun getDynamicColors(): DynamicColors = config.getValue()
        override fun setDynamicColors(dynamicColors: DynamicColors) = config.setValue(dynamicColors)
    }
}