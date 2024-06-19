package com.example.scheduleappk.navigation.enter

import com.example.enter.EnterFragmentContract
import com.example.schedule.ScheduleFragment
import com.example.scheduleappk.R
import com.example.scheduleappk.navigation.NavigateRouter
import javax.inject.Inject

class EnterFragmentContractImpl @Inject constructor(
    private val navigateRouter: NavigateRouter
) : EnterFragmentContract {
    override fun navigateToScheduleScreen() {


        navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                com.example.values.R.anim.slide_in,
                com.example.values.R.anim.slide_out,
                com.example.values.R.anim.slide_in_back,
                com.example.values.R.anim.slide_out_back,
            )
            replace(R.id.container_main, ScheduleFragment())
            commit()
        }
    }
}