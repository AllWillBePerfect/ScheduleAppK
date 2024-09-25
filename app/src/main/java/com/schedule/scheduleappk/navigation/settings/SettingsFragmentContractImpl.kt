package com.schedule.scheduleappk.navigation.settings

import android.content.Intent
import com.schedule.enter.EnterFragment
import com.schedule.schedule.v2.container.NavigationDrawerContainerFragment
import com.schedule.scheduleappk.MainActivity
import com.schedule.scheduleappk.R
import com.schedule.scheduleappk.navigation.NavigateRouter
import com.schedule.settings.SettingsFragmentContract
import com.schedule.settings.dialogs.replace.add.FragmentAddReplaceGroup
import com.schedule.settings.options.multiple_group.MultipleGroupOptionFragment
import javax.inject.Inject

class SettingsFragmentContractImpl @Inject constructor(
    private val navigateRouter: NavigateRouter
) : SettingsFragmentContract {
    override fun navigateToAddSingleGroupScreen() {

        navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            setCustomAnimations(
                com.schedule.values.R.anim.slide_in,
                com.schedule.values.R.anim.slide_out,
                com.schedule.values.R.anim.slide_in_back,
                com.schedule.values.R.anim.slide_out_back,

                )
            replace(R.id.container_main, EnterFragment.newInstanceSingleMode())
            addToBackStack(null)
            commit()
        }

    }

    override fun navigateToAddReplaceGroupScreen() {
        navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            setCustomAnimations(
                com.schedule.values.R.anim.slide_in,
                com.schedule.values.R.anim.slide_out,
                com.schedule.values.R.anim.slide_in_back,
                com.schedule.values.R.anim.slide_out_back,

                )
            replace(R.id.container_main, FragmentAddReplaceGroup())
            addToBackStack(null)
            commit()
        }
    }

    override fun navigateToAddMultipleGroupScreen() {
        navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            setCustomAnimations(
                com.schedule.values.R.anim.slide_in,
                com.schedule.values.R.anim.slide_out,
                com.schedule.values.R.anim.slide_in_back,
                com.schedule.values.R.anim.slide_out_back,

                )
            replace(R.id.container_main, EnterFragment.newInstanceMultipleMode())
            addToBackStack(null)
            commit()
        }
    }

    override fun navigateToMultipleGroupOptionFragment() {
        navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            setCustomAnimations(
                com.schedule.values.R.anim.slide_in,
                com.schedule.values.R.anim.slide_out,
                com.schedule.values.R.anim.slide_in_back,
                com.schedule.values.R.anim.slide_out_back,
            )
            replace(R.id.container_main, MultipleGroupOptionFragment())
            addToBackStack(null)
            commit()
        }
    }

    override fun popBackStack() {
        navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            setCustomAnimations(
                com.schedule.values.R.anim.slide_in,
                com.schedule.values.R.anim.slide_out,
                com.schedule.values.R.anim.slide_in_back,
                com.schedule.values.R.anim.slide_out_back,
            )
            replace(R.id.container_main, NavigationDrawerContainerFragment())
            addToBackStack(null)
            commit()
        }
    }

    override fun intentReloadApp() {
        val intent = Intent(navigateRouter.requireActivity(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        navigateRouter.requireActivity().startActivity(intent)
        navigateRouter.requireActivity().overridePendingTransition(0, 0)
    }

    override fun recreateApp() = navigateRouter.requireActivity().recreate()
}