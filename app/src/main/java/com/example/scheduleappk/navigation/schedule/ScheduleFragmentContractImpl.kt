package com.example.scheduleappk.navigation.schedule

import com.example.schedule.ScheduleFragmentContract
import com.example.scheduleappk.R
import com.example.scheduleappk.navigation.NavigateRouter
import com.example.settings.SettingsFragment
import javax.inject.Inject

class ScheduleFragmentContractImpl @Inject constructor(
    private val navigateRouter: NavigateRouter
) : ScheduleFragmentContract {
    override fun navigateToSettingsScreen() {
        navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                com.example.values.R.anim.slide_in,
                com.example.values.R.anim.slide_out,
                com.example.values.R.anim.slide_in_back,
                com.example.values.R.anim.slide_out_back,

            )
            replace(R.id.container_main, SettingsFragment(), "schedule")
            addToBackStack(null)
            commit()
        }
    }
}