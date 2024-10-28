package com.schedulev2.schedule.v2.webview

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import com.schedulev2.schedule.databinding.V2FragmentWebViewBinding
import com.schedulev2.views.BaseFragment

class WebViewFragment : BaseFragment<V2FragmentWebViewBinding>(V2FragmentWebViewBinding::inflate) {

    private val viewModel by activityViewModels<WebViewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.urlLiveData.observe(viewLifecycleOwner) {
            it.event?.let { event ->
                val webView = binding.webView
                webView.webViewClient = WebViewClient()
                webView.settings.javaScriptEnabled = true
                webView.loadUrl(event)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.popBackStack()
        }
    }
}