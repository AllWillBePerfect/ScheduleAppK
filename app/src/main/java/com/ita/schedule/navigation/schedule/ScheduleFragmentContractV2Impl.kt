package com.ita.schedule.navigation.schedule

import com.schedulev2.schedule.v2.ScheduleFragmentContractV2
import com.schedulev2.schedule.v2.webview.WebViewFragment
import com.ita.schedule.navigation.NavigateRouter
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
                    com.schedulev2.values.R.anim.slide_in,
                    com.schedulev2.values.R.anim.slide_out,
                    com.schedulev2.values.R.anim.slide_in_back,
                    com.schedulev2.values.R.anim.slide_out_back,

                    )
                replace(com.schedulev2.schedule.R.id.navigationDrawerContainer, WebViewFragment(), "webView")
                addToBackStack(null)
                commit()
            }
        }

    }
}