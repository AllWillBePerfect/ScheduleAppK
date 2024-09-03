package com.example.sharpref.sources

import com.example.models.sharpref.DynamicColors
import com.example.sharpref.configs.SHCDynamicColors
import javax.inject.Inject

interface DynamicColorsContract {
    fun getDynamicColors(): DynamicColors
    fun setDynamicColors(dynamicColors: DynamicColors)

    class Impl @Inject constructor(private val config: SHCDynamicColors) : DynamicColorsContract {
        override fun getDynamicColors(): DynamicColors = config.getValue()
        override fun setDynamicColors(dynamicColors: DynamicColors) = config.setValue(dynamicColors)
    }
}