package com.schedule.sharpref.di

import com.schedule.sharpref.sources.v1.AppConfigContract
import com.schedule.sharpref.sources.v1.AppConfigContractImpl
import com.schedule.sharpref.sources.DynamicColorsContract
import com.schedule.sharpref.sources.NightModeContract
import com.schedule.sharpref.sources.v2.AppConfigV2Contract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SharedPreferencesModule {

    @Binds
    @Singleton
    fun bindAppConfigSettings(appConfigSettingsImpl: AppConfigContractImpl): AppConfigContract

    @Binds
    @Singleton
    fun bindNightModeSettings(nightModeContractImpl: NightModeContract.Impl): NightModeContract

    @Binds
    @Singleton
    fun bindDynamicColorsSettings(dynamicColorsContractImpl: DynamicColorsContract.Impl): DynamicColorsContract

    @Binds
    @Singleton
    fun bindAppConfigV2Settings(appConfigV2ContractImpl: AppConfigV2Contract.Impl): AppConfigV2Contract
}