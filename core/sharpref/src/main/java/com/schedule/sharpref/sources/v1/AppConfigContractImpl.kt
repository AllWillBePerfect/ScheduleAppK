package com.schedule.sharpref.sources.v1

import com.schedule.sharpref.configs.v1.SHCAppConfig
import com.schedule.models.sharpref.v1.AppConfig
import javax.inject.Inject

class AppConfigContractImpl @Inject constructor(private val shcAppConfig: SHCAppConfig):
    AppConfigContract {

    override fun getAppConfig(): AppConfig = shcAppConfig.getValue()
    override fun setAppConfig(appConfig: AppConfig) = shcAppConfig.setValue(appConfig)

}