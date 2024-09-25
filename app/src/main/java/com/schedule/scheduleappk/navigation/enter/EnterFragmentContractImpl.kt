package com.schedule.scheduleappk.navigation.enter

import android.os.Bundle
import com.schedule.enter.EnterFragmentContract
import com.schedule.schedule.v2.ScheduleFragmentV2
import com.schedule.scheduleappk.R
import com.schedule.scheduleappk.navigation.NavigateRouter
import javax.inject.Inject

class EnterFragmentContractImpl @Inject constructor(
    private val navigateRouter: NavigateRouter
) : EnterFragmentContract {
    override fun navigateToScheduleScreen(
        instanceState: Bundle?
    ) {

//        if (instanceState == null)
            navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(
                    com.schedule.values.R.anim.slide_in,
                    com.schedule.values.R.anim.slide_out,
                    com.schedule.values.R.anim.slide_in_back,
                    com.schedule.values.R.anim.slide_out_back,
                )
                replace(R.id.container_main, ScheduleFragmentV2(), "Распсиание")
                commit()
            }
//        else {
//            val fragment =
//                navigateRouter.requireActivity().supportFragmentManager.findFragmentByTag("Распсиание")
//            if (fragment != null)
//                navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
//                    setCustomAnimations(
//                        com.example.values.R.anim.slide_in,
//                        com.example.values.R.anim.slide_out,
//                        com.example.values.R.anim.slide_in_back,
//                        com.example.values.R.anim.slide_out_back,
//                    )
//                    replace(R.id.container_main, fragment, "Распсиание")
//                    commit()
//                }
//        }
    }
}