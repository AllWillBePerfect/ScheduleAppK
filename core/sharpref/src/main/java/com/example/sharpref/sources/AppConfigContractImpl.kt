package com.example.sharpref.sources

import com.example.sharpref.configs.SHCAppConfig
import com.example.models.sharpref.AppConfig
import javax.inject.Inject

class AppConfigContractImpl @Inject constructor(private val shcAppConfig: SHCAppConfig): AppConfigContract{

    override fun getAppConfig(): AppConfig = shcAppConfig.getValue()
    override fun setAppConfig(appConfig: AppConfig) = shcAppConfig.setValue(appConfig)

}