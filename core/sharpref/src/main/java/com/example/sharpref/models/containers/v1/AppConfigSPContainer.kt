package com.example.sharpref.models.containers.v1

import com.example.models.sharpref.v1.AppConfig
import com.example.models.sharpref.v2.MultipleStorage
import com.example.models.sharpref.v2.ReplaceStorage
import com.example.models.sharpref.v2.SingleStorage


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

