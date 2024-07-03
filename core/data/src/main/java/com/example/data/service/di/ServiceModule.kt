package com.example.data.service.di

import com.example.data.service.RefreshService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ServiceModule {

    @Singleton
    @Binds
    fun bindsRefreshService(impl: RefreshService.Impl): RefreshService
}