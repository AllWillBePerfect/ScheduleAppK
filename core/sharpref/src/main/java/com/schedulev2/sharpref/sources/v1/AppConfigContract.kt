package com.schedulev2.sharpref.sources.v1

import com.schedulev2.models.sharpref.v1.AppConfig


interface AppConfigContract {
    fun getAppConfig(): AppConfig
    fun setAppConfig(appConfig: AppConfig)
}