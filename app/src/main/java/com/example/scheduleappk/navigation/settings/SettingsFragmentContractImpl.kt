package com.example.scheduleappk.navigation.settings

import com.example.enter.EnterFragment
import com.example.scheduleappk.R
import com.example.scheduleappk.navigation.NavigateRouter
import com.example.settings.SettingsFragmentContract
import com.example.settings.options.multiple_group.MultipleGroupOptionFragment
import javax.inject.Inject

class SettingsFragmentContractImpl @Inject constructor(
    private val navigateRouter: NavigateRouter
) : SettingsFragmentContract {
    override fun navigateToAddSingleGroupScreen() {

        navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            setCustomAnimations(
                com.example.values.R.anim.slide_in,
                com.example.values.R.anim.slide_out,
                com.example.values.R.anim.slide_in_back,
                com.example.values.R.anim.slide_out_back,

                )
            replace(R.id.container_main, EnterFragment.newInstance(true))
            addToBackStack(null)
            commit()
        }

    }

    override fun navigateToMultipleGroupOptionFragment() {
        navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            setCustomAnimations(
                com.example.values.R.anim.slide_in,
                com.example.values.R.anim.slide_out,
                com.example.values.R.anim.slide_in_back,
                com.example.values.R.anim.slide_out_back,
                )
            replace(R.id.container_main, MultipleGroupOptionFragment())
            addToBackStack(null)
            commit()
        }
    }
}