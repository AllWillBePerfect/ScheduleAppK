package com.schedule.scheduleappk.navigation.schedule

import com.schedule.schedule.v2.ScheduleFragmentContractV2
import com.schedule.schedule.v2.webview.WebViewFragment
import com.schedule.scheduleappk.R
import com.schedule.scheduleappk.navigation.NavigateRouter
import javax.inject.Inject

class ScheduleFragmentContractV2Impl @Inject constructor(
    private val navigateRouter: NavigateRouter
) : ScheduleFragmentContractV2 {
    override fun launchWebView() {

        val fragment = navigateRouter.requireActivity().supportFragmentManager.findFragmentByTag("MainActivity")
        fragment?.let {
            it.childFragmentManager.beginTransaction().apply {
                setReorderingAllowed(true)
                setCustomAnimations(
                    com.schedule.values.R.anim.slide_in,
                    com.schedule.values.R.anim.slide_out,
                    com.schedule.values.R.anim.slide_in_back,
                    com.schedule.values.R.anim.slide_out_back,

                    )
                replace(com.schedule.schedule.R.id.navigationDrawerContainer, WebViewFragment(), "webView")
                addToBackStack(null)
                commit()
            }
        }

    }
}