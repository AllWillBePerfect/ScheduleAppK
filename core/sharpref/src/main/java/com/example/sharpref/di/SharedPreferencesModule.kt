package com.example.sharpref.di

import com.example.sharpref.sources.AppConfigContract
import com.example.sharpref.sources.AppConfigContractImpl
import com.example.sharpref.sources.NightModeContract
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
}