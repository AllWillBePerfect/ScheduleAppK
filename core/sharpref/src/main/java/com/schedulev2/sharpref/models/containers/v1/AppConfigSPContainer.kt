package com.schedulev2.sharpref.models.containers.v1

import com.schedulev2.models.sharpref.v1.AppConfig
import com.schedulev2.models.sharpref.v2.MultipleStorage
import com.schedulev2.models.sharpref.v2.ReplaceStorage
import com.schedulev2.models.sharpref.v2.SingleStorage


data class AppConfigSPContainer(
    val appConfig: AppConfig
)

data class SingleStorageSPContainer(
    val singleStorage: SingleStorage
)

data class ReplaceStorageSPContainer(
    val replaceStorage: ReplaceStorage
)

data class MultipleStorageSPContainer(
    val multipleStorage: MultipleStorage
)

