package com.schedulev2.scheduleappk.navigation.schedule

import com.schedulev2.schedule.v1.ScheduleFragmentContract
import com.schedulev2.scheduleappk.R
import com.schedulev2.scheduleappk.navigation.NavigateRouter
import com.schedulev2.settings.SettingsFragment
import javax.inject.Inject

class ScheduleFragmentContractImpl @Inject constructor(
    private val navigateRouter: NavigateRouter
) : ScheduleFragmentContract {
    override fun navigateToSettingsScreen() {
        navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            setCustomAnimations(
                com.schedulev2.values.R.anim.slide_in,
                com.schedulev2.values.R.anim.slide_out,
                com.schedulev2.values.R.anim.slide_in_back,
                com.schedulev2.values.R.anim.slide_out_back,

            )
            replace(R.id.container_main, SettingsFragment(), "schedule")
            addToBackStack(null)
            commit()
        }
    }
}