package com.schedule.schedule.v2.webview

import androidx.lifecycle.ViewModel
import com.schedule.data.event_manager.LaunchWebViewEventManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    private val launchWebViewEventManager: LaunchWebViewEventManager,
) : ViewModel() {

    val urlLiveData = launchWebViewEventManager.getLaunchWebViewLiveData()
}