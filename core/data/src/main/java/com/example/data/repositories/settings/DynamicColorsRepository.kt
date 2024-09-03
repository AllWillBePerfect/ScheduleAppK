package com.example.data.repositories.settings

import com.example.models.sharpref.DynamicColors
import com.example.sharpref.sources.DynamicColorsContract
import javax.inject.Inject

interface DynamicColorsRepository {

    fun getDynamicColorsState(): Boolean
    fun setDynamicColors(isEnabled: Boolean)
    fun changeDynamicColors()

    class Impl @Inject constructor(
        private val dynamicColorsContract: DynamicColorsContract
    ) : DynamicColorsRepository {
        override fun getDynamicColorsState(): Boolean =
            dynamicColorsContract.getDynamicColors().isEnabled

        override fun setDynamicColors(isEnabled: Boolean) =
            dynamicColorsContract.setDynamicColors(DynamicColors(isEnabled))

        override fun changeDynamicColors() = setDynamicColors(!getDynamicColorsState())

    }
}