package com.schedulev2.sharpref.di

import com.schedulev2.sharpref.sources.AdditionalOptionsContract
import com.schedulev2.sharpref.sources.v1.AppConfigContract
import com.schedulev2.sharpref.sources.v1.AppConfigContractImpl
import com.schedulev2.sharpref.sources.DynamicColorsContract
import com.schedulev2.sharpref.sources.NightModeContract
import com.schedulev2.sharpref.sources.v2.AppConfigV2Contract
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

    @Binds
    @Singleton
    fun bindAdditionalOptionsSettings(additionalOptionsContract: AdditionalOptionsContract.Impl): AdditionalOptionsContract
}