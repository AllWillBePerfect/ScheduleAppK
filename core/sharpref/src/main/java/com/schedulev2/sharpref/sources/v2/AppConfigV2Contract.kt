package com.schedulev2.sharpref.sources.v2

import com.schedulev2.models.sharpref.v2.AppConfigV2
import com.schedulev2.models.sharpref.v2.MultipleStorage
import com.schedulev2.models.sharpref.v2.ReplaceStorage
import com.schedulev2.models.sharpref.v2.SingleStorage
import com.schedulev2.sharpref.configs.v2.SHCAppConfigV2
import com.schedulev2.sharpref.configs.v2.SHCMultipleStorage
import com.schedulev2.sharpref.configs.v2.SHCReplaceStorage
import com.schedulev2.sharpref.configs.v2.SHCSingleStorage
import javax.inject.Inject

interface AppConfigV2Contract {

    fun getAppConfigV2(): AppConfigV2
    fun setAppConfigV2(appConfigV2: AppConfigV2)

    fun getSingleStorage(): SingleStorage
    fun setSingleStorage(singleStorage: SingleStorage)

    fun getReplaceStorage(): ReplaceStorage
    fun setReplaceStorage(replaceStorage: ReplaceStorage)

    fun getMultipleStorage(): MultipleStorage
    fun setMultipleStorage(multipleStorage: MultipleStorage)

    class Impl @Inject constructor(
        private val shcAppConfigV2: SHCAppConfigV2,
        private val shcSingleStorage: SHCSingleStorage,
        private val shcReplaceStorage: SHCReplaceStorage,
        private val shcMultipleStorage: SHCMultipleStorage
    ): AppConfigV2Contract {
        override fun getAppConfigV2(): AppConfigV2 = shcAppConfigV2.getValue()
        override fun setAppConfigV2(appConfigV2: AppConfigV2) = shcAppConfigV2.setValue(appConfigV2)

        override fun getSingleStorage(): SingleStorage = shcSingleStorage.getValue()
        override fun setSingleStorage(singleStorage: SingleStorage) = shcSingleStorage.setValue(singleStorage)

        override fun getReplaceStorage(): ReplaceStorage = shcReplaceStorage.getValue()
        override fun setReplaceStorage(replaceStorage: ReplaceStorage) = shcReplaceStorage.setValue(replaceStorage)

        override fun getMultipleStorage(): MultipleStorage = shcMultipleStorage.getValue()
        override fun setMultipleStorage(multipleStorage: MultipleStorage) = shcMultipleStorage.setValue(multipleStorage)
    }
}