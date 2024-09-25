package com.schedule.data.repositories.di

import android.content.Context
import com.schedule.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoriesModuleProvide {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = AppDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun provideDatabaseDao(db: AppDatabase) = db.scheduleDao()

//    @Provides
//    @SingleImplementation
//    fun provideSingleImplementations(
//        currentLessonRepository: CurrentLessonRepository,
//        scheduleApiRepository: ScheduleApiRepository,
//        appConfigRepositoryV2: AppConfigRepositoryV2
//    ): ScheduleRepository {
//        return SingleImpl(
//            currentLessonRepository,
//            scheduleApiRepository,
//            appConfigRepositoryV2
//        )
//    }
//
//    @Provides
//    @ReplaceImplementation
//    fun provideReplaceImplementations(
//        currentLessonRepository: CurrentLessonRepository,
//        scheduleApiRepository: ScheduleApiRepository,
//        appConfigRepository: AppConfigRepository
//    ): ScheduleRepository {
//        return ReplaceImpl(
//            currentLessonRepository,
//            scheduleApiRepository,
//            appConfigRepository
//        )
//    }
//
//    @Provides
//    @MultipleImplementation
//    fun provideMultipleImplementations(
//        currentLessonRepository: CurrentLessonRepository,
//        scheduleApiRepository: ScheduleApiRepository,
//        appConfigRepository: AppConfigRepository
//    ): ScheduleRepository {
//        return MultipleImpl(
//            currentLessonRepository,
//            scheduleApiRepository,
//            appConfigRepository
//        )
//    }
}