package com.schedulev2.sharpref.configs

import android.content.Context
import com.schedulev2.models.sharpref.NightMode
import com.schedulev2.sharpref.models.containers.NightModeSPContainer
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SHCNightMode @Inject constructor(@ApplicationContext context: Context) :
    SettingsHolderConfig<NightMode, NightModeSPContainer>(
        context
    ) {
    override fun getSPValueName(): String = "night_mode_sp"

    override fun getDefaultValue(): NightModeSPContainer = NightModeSPContainer(
        nightMode = NightMode.FOLLOW_SYSTEM
    )

    override fun getAdapter(): JsonAdapter<NightModeSPContainer> = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory()).build().adapter(NightModeSPContainer::class.java)

    override fun mapFromValue(value: NightMode): NightModeSPContainer = NightModeSPContainer(value)

    override fun mapToValue(sspContainer: NightModeSPContainer): NightMode = sspContainer.nightMode
}