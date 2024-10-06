package com.schedule.sharpref.configs

import android.content.Context
import com.schedule.models.sharpref.AdditionalOptions
import com.schedule.sharpref.models.containers.AdditionalOptionsSPContainer
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SHCAdditionalOptions @Inject constructor(@ApplicationContext context: Context) :
    SettingsHolderConfig<AdditionalOptions, AdditionalOptionsSPContainer>(context) {
    override fun getSPValueName(): String = "additional_options_sp"

    override fun getDefaultValue(): AdditionalOptionsSPContainer = AdditionalOptionsSPContainer(
        AdditionalOptions(
            multipleGroupFastScroll = true
        )
    )

    override fun getAdapter(): JsonAdapter<AdditionalOptionsSPContainer> = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory()).build().adapter(AdditionalOptionsSPContainer::class.java)

    override fun mapFromValue(value: AdditionalOptions): AdditionalOptionsSPContainer = AdditionalOptionsSPContainer(value)

    override fun mapToValue(sspContainer: AdditionalOptionsSPContainer): AdditionalOptions = sspContainer.additionalOptions
}