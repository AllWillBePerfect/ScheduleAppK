package com.ita.schedule.di

import com.schedulev2.clear.ClearFragmentContract
import com.schedulev2.enter.EnterFragmentContract
import com.schedulev2.schedule.v1.ScheduleFragmentContract
import com.schedulev2.schedule.v2.ScheduleFragmentContractV2
import com.ita.schedule.navigation.clear.ClearFragmentContractImpl
import com.ita.schedule.navigation.enter.EnterFragmentContractImpl
import com.ita.schedule.navigation.schedule.ScheduleFragmentContractImpl
import com.ita.schedule.navigation.schedule.ScheduleFragmentContractV2Impl
import com.ita.schedule.navigation.settings.SettingsFragmentContractImpl
import com.schedulev2.settings.SettingsFragmentContract
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
    fun bindScheduleFragmentContractV2(scheduleFragmentContractV2Impl: ScheduleFragmentContractV2Impl): ScheduleFragmentContractV2

    @Binds
    fun bindSettingsFragmentContract(settingsFragmentContractImpl: SettingsFragmentContractImpl): SettingsFragmentContract

    @Binds
    fun bindClearFragmentContract(clearFragmentContractImpl: ClearFragmentContractImpl): ClearFragmentContract
}