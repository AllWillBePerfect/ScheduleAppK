package com.example.data.repositories.di

import com.example.data.repositories.AppConfigRepository
import com.example.data.repositories.ClearDataRepository
import com.example.data.repositories.CurrentLessonRepository
import com.example.data.repositories.ScheduleApiRepository
import com.example.data.repositories.ScheduleItemListRepository
import com.example.data.repositories.ScheduleItemListRepositoryV2
import com.example.data.repositories.ScheduleItemListRepositoryV3
import com.example.data.repositories.SettingsOptionRepository
import com.example.data.repositories.TimeProgressIndicatorRepository
import com.example.data.repositories.settings.DynamicColorsRepository
import com.example.data.repositories.v2.schedule.repository.impl.AppConfigRepositoryV2
import com.example.data.repositories.v2.schedule.services.WriteAndSearchListOfGroupsService
import com.example.data.repositories.v2.schedule.repository.impl.MultipleImpl
import com.example.data.repositories.v2.schedule.repository.impl.ReplaceImpl
import com.example.data.repositories.v2.schedule.repository.impl.SingleImpl
import com.example.data.repositories.v2.schedule.repository.impl.ContainerImpl
import com.example.data.repositories.v2.schedule.repository.ScheduleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
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

    @Binds
    @Singleton
    fun bindsTimeProgressIndicatorRepository(impl: TimeProgressIndicatorRepository.Impl): TimeProgressIndicatorRepository

    @Binds
    @Singleton
    fun bindsCurrentLessonRepository(impl: CurrentLessonRepository.Impl): CurrentLessonRepository

    @Binds
    @Singleton
    fun bindsScheduleItemListRepository(impl: ScheduleItemListRepository.Impl): ScheduleItemListRepository

    @Binds
    @Singleton
    fun bindsScheduleItemListRepositoryV2(impl: ScheduleItemListRepositoryV2.Impl): ScheduleItemListRepositoryV2

    @Binds
    @Singleton
    fun bindsScheduleItemListRepositoryV3(impl: ScheduleItemListRepositoryV3.Impl): ScheduleItemListRepositoryV3

    @Binds
    @Singleton
    fun bindsSettingsOptionRepository(impl: SettingsOptionRepository.Impl): SettingsOptionRepository

    @Binds
    @Singleton
    fun bindsClearDataRepository(impl: ClearDataRepository.Impl): ClearDataRepository

    @Binds
    @Singleton
    fun bindsDynamicColorsRepository(impl: DynamicColorsRepository.Impl): DynamicColorsRepository


    @Binds
    @Singleton
    @SingleImplementation
    fun bindsSingleScheduleRepository(singleImpl: SingleImpl): ScheduleRepository

    @Binds
    @Singleton
    @ReplaceImplementation
    fun bindsReplaceScheduleRepository(replaceImpl: ReplaceImpl): ScheduleRepository

    @Binds
    @Singleton
    @MultipleImplementation
    fun bindsMultipleScheduleRepository(multipleImpl: MultipleImpl): ScheduleRepository

    @Binds
    @Singleton
    @ContainerImplementation
    fun bindsContainerScheduleRepository(containerImpl: ContainerImpl): ScheduleRepository

    @Binds
    @Singleton
    fun bindAppConfigRepositoryV2(impl: AppConfigRepositoryV2.Impl): AppConfigRepositoryV2

}

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryViewModelModule {

    @Binds
    fun bindsWriteAndSearchListOfGroupsService(impl: WriteAndSearchListOfGroupsService.Impl): WriteAndSearchListOfGroupsService

}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SingleImplementation

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ReplaceImplementation

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MultipleImplementation

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ContainerImplementation