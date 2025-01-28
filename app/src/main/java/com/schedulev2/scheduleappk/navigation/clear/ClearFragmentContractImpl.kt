package com.schedulev2.scheduleappk.navigation.clear

import com.schedulev2.clear.ClearFragmentContract
import com.schedulev2.enter.EnterFragment
import com.schedulev2.scheduleappk.R
import com.schedulev2.scheduleappk.navigation.NavigateRouter
import javax.inject.Inject

class ClearFragmentContractImpl @Inject constructor(
    private val navigateRouter: NavigateRouter
) : ClearFragmentContract {
    override fun launchAuthScreen() {
        navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            setCustomAnimations(
                com.schedulev2.values.R.anim.slide_in,
                com.schedulev2.values.R.anim.slide_out,
                com.schedulev2.values.R.anim.slide_in_back,
                com.schedulev2.values.R.anim.slide_out_back,

                )
            replace(R.id.container_main, EnterFragment.newInstance())
            commit()
        }
    }
}