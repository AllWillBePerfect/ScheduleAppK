package com.example.scheduleappk.di

import com.example.clear.ClearFragmentContract
import com.example.enter.EnterFragmentContract
import com.example.schedule.ScheduleFragmentContract
import com.example.scheduleappk.navigation.clear.ClearFragmentContractImpl
import com.example.scheduleappk.navigation.enter.EnterFragmentContractImpl
import com.example.scheduleappk.navigation.schedule.ScheduleFragmentContractImpl
import com.example.scheduleappk.navigation.settings.SettingsFragmentContractImpl
import com.example.settings.SettingsFragmentContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
interface ModuleBinds {

    @Binds
    fun bindEnterFragmentContract(enterFragmentContractImpl: EnterFragmentContractImpl): EnterFragmentContract

    @Binds
    fun bindScheduleFragmentContract(scheduleFragmentContractImpl: ScheduleFragmentContractImpl): ScheduleFragmentContract

    @Binds
    fun bindSettingsFragmentContract(settingsFragmentContractImpl: SettingsFragmentContractImpl): SettingsFragmentContract

    @Binds
    fun bindClearFragmentContract(clearFragmentContractImpl: ClearFragmentContractImpl): ClearFragmentContract
}