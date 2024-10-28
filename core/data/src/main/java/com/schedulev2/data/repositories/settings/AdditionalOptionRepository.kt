package com.schedulev2.data.repositories.settings

import com.schedulev2.sharpref.sources.AdditionalOptionsContract
import javax.inject.Inject

interface AdditionalOptionRepository {

    fun getMultipleGroupFastScrollState(): Boolean
    fun switchMultipleGroupFastScrollState()

    fun getLaunchInWebViewState(): Boolean
    fun switchLaunchInWebViewState()

    class Impl @Inject constructor(
        private val additionalOptionContract: AdditionalOptionsContract
    ) : AdditionalOptionRepository {
        override fun getMultipleGroupFastScrollState(): Boolean =
            additionalOptionContract.getAdditionalOptions().multipleGroupFastScroll

        override fun switchMultipleGroupFastScrollState() =
            additionalOptionContract.setAdditionalOptions(
                additionalOptionContract.getAdditionalOptions()
                    .copy(multipleGroupFastScroll = !getMultipleGroupFastScrollState())
            )

        override fun getLaunchInWebViewState(): Boolean =
            additionalOptionContract.getAdditionalOptions().launchInWebView

        override fun switchLaunchInWebViewState() = additionalOptionContract.setAdditionalOptions(
            additionalOptionContract.getAdditionalOptions().copy(
                launchInWebView = !getLaunchInWebViewState()
            )
        )


    }
}