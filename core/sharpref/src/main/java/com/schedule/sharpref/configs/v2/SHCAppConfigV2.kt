package com.schedule.sharpref.configs.v2

import android.content.Context
import com.schedule.models.sharpref.v2.AppConfigV2
import com.schedule.models.sharpref.v2.AppStateV2
import com.schedule.sharpref.configs.SettingsHolderConfig
import com.schedule.sharpref.models.containers.v2.AppConfigV2SPContainer
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SHCAppConfigV2 @Inject constructor(@ApplicationContext context: Context) :
    SettingsHolderConfig<AppConfigV2, AppConfigV2SPContainer>(context) {
    override fun getSPValueName(): String = "app_config_sp_v2"

    override fun getDefaultValue(): AppConfigV2SPContainer = AppConfigV2SPContainer(
        AppConfigV2(
            currentState = AppStateV2.Unselected("qwerty")
        )
    )

    override fun getAdapter(): JsonAdapter<AppConfigV2SPContainer> =
        Moshi.Builder().add(
            PolymorphicJsonAdapterFactory.of(AppStateV2::class.java, "type")
                .withSubtype(AppStateV2.Single::class.java, "Single")
                .withSubtype(AppStateV2.Replace::class.java, "Replace")
                .withSubtype(AppStateV2.Multiple::class.java, "Multiple")
                .withSubtype(AppStateV2.Unselected::class.java, "Unselected")
        ).addLast(KotlinJsonAdapterFactory()).build().adapter(AppConfigV2SPContainer::class.java)

    override fun mapFromValue(value: AppConfigV2): AppConfigV2SPContainer =
        AppConfigV2SPContainer(value)

    override fun mapToValue(sspContainer: AppConfigV2SPContainer): AppConfigV2 =
        sspContainer.appConfig
}