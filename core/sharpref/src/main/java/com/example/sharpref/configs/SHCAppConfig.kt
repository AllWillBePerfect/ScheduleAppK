package com.example.sharpref.configs

import android.content.Context
import com.example.models.sharpref.AppConfig
import com.example.models.sharpref.AppState
import com.example.models.sharpref.MultipleGroup
import com.example.models.sharpref.SingleGroup
import com.example.sharpref.models.containers.AppConfigSPContainer
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
            SingleGroup(""),
            MultipleGroup("", "", emptyList())
        )
    )

    override fun getAdapter(): JsonAdapter<AppConfigSPContainer> =
        Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(AppConfigSPContainer::class.java)

    override fun mapFromValue(value: AppConfig): AppConfigSPContainer =
        AppConfigSPContainer(value)

    override fun mapToValue(sspContainer: AppConfigSPContainer): AppConfig =
        sspContainer.appConfig
}