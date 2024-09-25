package com.schedule.scheduleappk.navigation.clear

import com.schedule.clear.ClearFragmentContract
import com.schedule.enter.EnterFragment
import com.schedule.scheduleappk.R
import com.schedule.scheduleappk.navigation.NavigateRouter
import javax.inject.Inject

class ClearFragmentContractImpl @Inject constructor(
    private val navigateRouter: NavigateRouter
) : ClearFragmentContract {
    override fun launchAuthScreen() {
        navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            setCustomAnimations(
                com.schedule.values.R.anim.slide_in,
                com.schedule.values.R.anim.slide_out,
                com.schedule.values.R.anim.slide_in_back,
                com.schedule.values.R.anim.slide_out_back,

                )
            replace(R.id.container_main, EnterFragment.newInstance())
            commit()
        }
    }
}