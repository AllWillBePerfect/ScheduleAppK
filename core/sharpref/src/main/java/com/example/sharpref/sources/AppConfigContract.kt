package com.example.sharpref.sources

import com.example.models.sharpref.AppConfig


interface AppConfigContract {
    fun getAppConfig(): AppConfig
    fun setAppConfig(appConfig: AppConfig)
}