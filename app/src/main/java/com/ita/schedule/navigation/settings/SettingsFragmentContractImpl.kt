package com.ita.schedule.navigation.settings

import android.content.Intent
import com.schedulev2.enter.EnterFragment
import com.schedulev2.schedule.v2.container.NavigationDrawerContainerFragment
import com.ita.schedule.MainActivity
import com.ita.schedule.R
import com.ita.schedule.navigation.NavigateRouter
import com.schedulev2.settings.SettingsFragmentContract
import com.schedulev2.settings.dialogs.replace.add.FragmentAddReplaceGroup
import com.schedulev2.settings.modal.ChangeDaysModal
import com.schedulev2.settings.options.multiple_group.MultipleGroupOptionFragment
import javax.inject.Inject

class SettingsFragmentContractImpl @Inject constructor(
    private val navigateRouter: NavigateRouter
) : SettingsFragmentContract {
    override fun navigateToAddSingleGroupScreen() {

        navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            setCustomAnimations(
                com.schedulev2.values.R.anim.slide_in,
                com.schedulev2.values.R.anim.slide_out,
                com.schedulev2.values.R.anim.slide_in_back,
                com.schedulev2.values.R.anim.slide_out_back,

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
                com.schedulev2.values.R.anim.slide_in,
                com.schedulev2.values.R.anim.slide_out,
                com.schedulev2.values.R.anim.slide_in_back,
                com.schedulev2.values.R.anim.slide_out_back,

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
                com.schedulev2.values.R.anim.slide_in,
                com.schedulev2.values.R.anim.slide_out,
                com.schedulev2.values.R.anim.slide_in_back,
                com.schedulev2.values.R.anim.slide_out_back,

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
                com.schedulev2.values.R.anim.slide_in,
                com.schedulev2.values.R.anim.slide_out,
                com.schedulev2.values.R.anim.slide_in_back,
                com.schedulev2.values.R.anim.slide_out_back,
            )
            replace(R.id.container_main, MultipleGroupOptionFragment())
            addToBackStack(null)
            commit()
        }
    }

    override fun launchChangeModal() {
        val modal = ChangeDaysModal()
        modal.show(navigateRouter.requireActivity().supportFragmentManager, ChangeDaysModal.TAG)
    }

    override fun popBackStack() {
        navigateRouter.requireActivity().supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            setCustomAnimations(
                com.schedulev2.values.R.anim.slide_in,
                com.schedulev2.values.R.anim.slide_out,
                com.schedulev2.values.R.anim.slide_in_back,
                com.schedulev2.values.R.anim.slide_out_back,
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