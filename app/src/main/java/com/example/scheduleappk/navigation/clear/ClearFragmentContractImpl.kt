package com.example.scheduleappk.navigation.clear

import com.example.clear.ClearFragmentContract
import com.example.enter.EnterFragment
import com.example.scheduleappk.R
import com.example.scheduleappk.navigation.NavigateRouter
import javax.inject.Inject

class ClearFragmentContractImpl @Inject constructor(
    private val navigateRouter: NavigateRouter
) : ClearFragmentContract {
    override fun launchAuthScreen() {
        navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            setCustomAnimations(
                com.example.values.R.anim.slide_in,
                com.example.values.R.anim.slide_out,
                com.example.values.R.anim.slide_in_back,
                com.example.values.R.anim.slide_out_back,

                )
            replace(R.id.container_main, EnterFragment.newInstance(false))
            commit()
        }
    }
}