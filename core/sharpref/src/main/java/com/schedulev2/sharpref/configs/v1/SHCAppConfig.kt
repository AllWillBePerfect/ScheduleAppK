package com.schedulev2.sharpref.configs.v1

import android.content.Context
import com.schedulev2.models.sharpref.v1.AppConfig
import com.schedulev2.models.sharpref.v1.AppState
import com.schedulev2.models.sharpref.v1.MultipleGroup
import com.schedulev2.models.sharpref.v1.MultipleGroupConfig
import com.schedulev2.models.sharpref.v1.SingleGroup
import com.schedulev2.models.sharpref.v1.SingleGroupConfig
import com.schedulev2.sharpref.configs.SettingsHolderConfig
import com.schedulev2.sharpref.models.containers.v1.AppConfigSPContainer
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SHCAppConfig @Inject constructor(@ApplicationContext context: Context) :
    SettingsHolderConfig<AppConfig, AppConfigSPContainer>(context) {
    override fun getSPValueName(): String = "app_config_sp"

    override fun getDefaultValue(): AppConfigSPContainer = AppConfigSPContainer(
        AppConfig(
            AppState.UNSELECT,
            SingleGroupConfig(
                SingleGroup(""),
                mutableListOf()),
            MultipleGroupConfig(
                MultipleGroup("", "", emptyList()),
                mutableListOf(),
                listOf(1,2,3,4,5,6)
            )
        )
    )

    override fun getAdapter(): JsonAdapter<AppConfigSPContainer> =
        Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            .adapter(AppConfigSPContainer::class.java)

    override fun mapFromValue(value: AppConfig): AppConfigSPContainer =
        AppConfigSPContainer(value)

    override fun mapToValue(sspContainer: AppConfigSPContainer): AppConfig =
        sspContainer.appConfig
}