package com.example.data.repositories.di

import com.example.data.repositories.AppConfigRepository
import com.example.data.repositories.ScheduleApiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {

    @Binds
    @Singleton
    fun bindsAppConfigRepository(impl: AppConfigRepository.Impl): AppConfigRepository

    @Binds
    @Singleton
    fun bindsScheduleApiRepository(impl: ScheduleApiRepository.Impl): ScheduleApiRepository
}