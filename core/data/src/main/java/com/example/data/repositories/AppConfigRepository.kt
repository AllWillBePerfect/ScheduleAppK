package com.example.data.repositories




import com.example.models.sharpref.AppState
import com.example.models.sharpref.MultipleGroup
import com.example.models.sharpref.SingleGroup
import com.example.sharpref.sources.AppConfigContract
import javax.inject.Inject

interface AppConfigRepository {

    fun changeAppStateToUnselect()
    fun changeAppStateToSingle()
    fun changeAppStateToMultiple()
    fun getAppState(): AppState

    fun setSingleGroup(groupName: String)
    fun getSingleGroup(): SingleGroup

    fun setMultipleGroup(groupNameFirstPosition: String, groupNameSecondPosition: String, daysToReplace: List<Int>)
    fun getMultipleGroup(): MultipleGroup

    class Impl @Inject constructor(private val appConfigSetting: AppConfigContract) :
        AppConfigRepository {

        private fun changeAppState(state: AppState) {
            var appConfig = appConfigSetting.getAppConfig()
            appConfig = appConfig.copy(appState = state)
            appConfigSetting.setAppConfig(appConfig)
        }
        private fun changeSingleGroup(singleGroup: SingleGroup) {
            var appConfig = appConfigSetting.getAppConfig()
            appConfig = appConfig.copy(appState = AppState.SINGLE, singleGroup = singleGroup)
            appConfigSetting.setAppConfig(appConfig)
        }
        private fun changeMultipleGroup(multipleGroup: MultipleGroup) {
            var appConfig = appConfigSetting.getAppConfig()
            appConfig = appConfig.copy(appState = AppState.MULTIPLE, multipleGroup = multipleGroup)
            appConfigSetting.setAppConfig(appConfig)
        }

        override fun changeAppStateToUnselect() = changeAppState(AppState.UNSELECT)
        override fun changeAppStateToSingle() = changeAppState(AppState.SINGLE)
        override fun changeAppStateToMultiple() = changeAppState(AppState.MULTIPLE)
        override fun getAppState(): AppState = appConfigSetting.getAppConfig().appState

        override fun setSingleGroup(groupName: String) = changeSingleGroup(
            SingleGroup(
                groupName
            )
        )
        override fun getSingleGroup(): SingleGroup = appConfigSetting.getAppConfig().singleGroup
        override fun setMultipleGroup(
            groupNameFirstPosition: String,
            groupNameSecondPosition: String,
            daysToReplace: List<Int>
        ) = changeMultipleGroup(
            MultipleGroup(
                groupNameFirstPosition,
                groupNameSecondPosition,
                daysToReplace
            )
        )

        override fun getMultipleGroup(): MultipleGroup = appConfigSetting.getAppConfig().multipleGroup
    }
}