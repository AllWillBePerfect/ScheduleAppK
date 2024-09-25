package com.example.sharpref.di

import com.example.sharpref.sources.v1.AppConfigContract
import com.example.sharpref.sources.v1.AppConfigContractImpl
import com.example.sharpref.sources.DynamicColorsContract
import com.example.sharpref.sources.NightModeContract
import com.example.sharpref.sources.v2.AppConfigV2Contract
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