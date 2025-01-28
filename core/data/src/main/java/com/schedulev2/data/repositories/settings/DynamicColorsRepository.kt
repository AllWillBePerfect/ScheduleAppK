package com.schedulev2.data.repositories.settings

import com.schedulev2.models.sharpref.DynamicColors
import com.schedulev2.sharpref.sources.DynamicColorsContract
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