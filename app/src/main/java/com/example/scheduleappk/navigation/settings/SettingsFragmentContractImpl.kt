package com.example.scheduleappk.navigation.settings

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.enter.EnterFragment
import com.example.scheduleappk.MainActivity
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

    override fun intentReloadApp() {
        val intent = Intent(navigateRouter.requireActivity(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        navigateRouter.requireActivity().startActivity(intent)
        navigateRouter.requireActivity().overridePendingTransition(0, 0)
    }

    override fun recreateApp() = navigateRouter.requireActivity().recreate()
}