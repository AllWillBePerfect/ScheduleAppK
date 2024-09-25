package com.schedule.sharpref.sources.v1

import com.schedule.models.sharpref.v1.AppConfig


interface AppConfigContract {
    fun getAppConfig(): AppConfig
    fun setAppConfig(appConfig: AppConfig)
}