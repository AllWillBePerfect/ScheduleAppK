package com.example.sharpref.sources.v1

import com.example.models.sharpref.v1.AppConfig


interface AppConfigContract {
    fun getAppConfig(): AppConfig
    fun setAppConfig(appConfig: AppConfig)
}