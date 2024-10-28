package com.schedulev2.scheduleappk.navigation.enter

import android.os.Bundle
import com.schedulev2.enter.EnterFragmentContract
import com.schedulev2.schedule.v2.ScheduleFragmentV2
import com.schedulev2.scheduleappk.R
import com.schedulev2.scheduleappk.navigation.NavigateRouter
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
                    com.schedulev2.values.R.anim.slide_in,
                    com.schedulev2.values.R.anim.slide_out,
                    com.schedulev2.values.R.anim.slide_in_back,
                    com.schedulev2.values.R.anim.slide_out_back,
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