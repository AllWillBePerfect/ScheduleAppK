package com.schedule.sharpref.sources

import com.schedule.models.sharpref.AdditionalOptions
import com.schedule.sharpref.configs.SHCAdditionalOptions
import javax.inject.Inject

interface AdditionalOptionsContract {

    fun getAdditionalOptions(): AdditionalOptions
    fun setAdditionalOptions(additionalOptions: AdditionalOptions)

    class Impl @Inject constructor(
        private val shcAdditionalOptions: SHCAdditionalOptions
    ): AdditionalOptionsContract {
        override fun getAdditionalOptions(): AdditionalOptions = shcAdditionalOptions.getValue()

        override fun setAdditionalOptions(additionalOptions: AdditionalOptions) = shcAdditionalOptions.setValue(additionalOptions)
    }
}