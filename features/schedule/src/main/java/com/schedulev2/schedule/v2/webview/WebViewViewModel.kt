package com.schedulev2.schedule.v2.webview

import androidx.lifecycle.ViewModel
import com.schedulev2.data.event_manager.LaunchWebViewEventManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    private val launchWebViewEventManager: LaunchWebViewEventManager,
) : ViewModel() {

    val urlLiveData = launchWebViewEventManager.getLaunchWebViewLiveData()
}