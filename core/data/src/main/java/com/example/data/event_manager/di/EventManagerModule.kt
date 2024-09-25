package com.example.data.event_manager.di

import com.example.data.event_manager.RefreshEventManager
import com.example.data.event_manager.RestoreAfterPopBackStackEventManager
import com.example.data.event_manager.RestoreDialogEventManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface EventManagerModule {

    @Singleton
    @Binds
    fun bindsRefreshEventManager(impl: RefreshEventManager.Impl): RefreshEventManager

    @Singleton
    @Binds
    fun bindsRestoreAfterPopBackSEventManager(impl: RestoreAfterPopBackStackEventManager.Impl): RestoreAfterPopBackStackEventManager

    @Singleton
    @Binds
    fun bindsRestoreDialogEventManager(impl: RestoreDialogEventManager.Impl): RestoreDialogEventManager

}