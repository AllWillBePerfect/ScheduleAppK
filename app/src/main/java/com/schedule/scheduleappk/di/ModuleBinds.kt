package com.schedule.scheduleappk.di

import com.schedule.clear.ClearFragmentContract
import com.schedule.enter.EnterFragmentContract
import com.schedule.schedule.v1.ScheduleFragmentContract
import com.schedule.scheduleappk.navigation.clear.ClearFragmentContractImpl
import com.schedule.scheduleappk.navigation.enter.EnterFragmentContractImpl
import com.schedule.scheduleappk.navigation.schedule.ScheduleFragmentContractImpl
import com.schedule.scheduleappk.navigation.settings.SettingsFragmentContractImpl
import com.schedule.settings.SettingsFragmentContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

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