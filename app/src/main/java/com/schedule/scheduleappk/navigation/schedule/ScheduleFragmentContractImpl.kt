package com.schedule.scheduleappk.navigation.schedule

import com.schedule.schedule.v1.ScheduleFragmentContract
import com.schedule.scheduleappk.R
import com.schedule.scheduleappk.navigation.NavigateRouter
import com.schedule.settings.SettingsFragment
import javax.inject.Inject

class ScheduleFragmentContractImpl @Inject constructor(
    private val navigateRouter: NavigateRouter
) : ScheduleFragmentContract {
    override fun navigateToSettingsScreen() {
        navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            setCustomAnimations(
                com.schedule.values.R.anim.slide_in,
                com.schedule.values.R.anim.slide_out,
                com.schedule.values.R.anim.slide_in_back,
                com.schedule.values.R.anim.slide_out_back,

            )
            replace(R.id.container_main, SettingsFragment(), "schedule")
            addToBackStack(null)
            commit()
        }
    }
}