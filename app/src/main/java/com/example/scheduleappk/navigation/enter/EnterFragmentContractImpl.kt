package com.example.scheduleappk.navigation.enter

import android.os.Bundle
import com.example.enter.EnterFragmentContract
import com.example.schedule.v1.ScheduleFragment
import com.example.scheduleappk.R
import com.example.scheduleappk.navigation.NavigateRouter
import javax.inject.Inject

class EnterFragmentContractImpl @Inject constructor(
    private val navigateRouter: NavigateRouter
) : EnterFragmentContract {
    override fun navigateToScheduleScreen(
        instanceState: Bundle?
    ) {

        if (instanceState == null)
            navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(
                    com.example.values.R.anim.slide_in,
                    com.example.values.R.anim.slide_out,
                    com.example.values.R.anim.slide_in_back,
                    com.example.values.R.anim.slide_out_back,
                )
                replace(R.id.container_main, ScheduleFragment(), "Распсиание")
                commit()
            }
        else {
            val fragment =
                navigateRouter.requireActivity().supportFragmentManager.findFragmentByTag("Распсиание")
            if (fragment != null)
                navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
                    setCustomAnimations(
                        com.example.values.R.anim.slide_in,
                        com.example.values.R.anim.slide_out,
                        com.example.values.R.anim.slide_in_back,
                        com.example.values.R.anim.slide_out_back,
                    )
                    replace(R.id.container_main, fragment, "Распсиание")
                    commit()
                }
        }
    }
}