package com.schedulev2.data.repositories


import com.schedulev2.models.sharpref.v1.AppState
import com.schedulev2.models.sharpref.v1.MultipleGroup
import com.schedulev2.models.sharpref.v1.SingleGroup
import com.schedulev2.sharpref.sources.v1.AppConfigContract
import javax.inject.Inject

interface AppConfigRepository {

    fun changeAppStateToUnselect()
    fun changeAppStateToSingle()
    fun changeAppStateToMultiple()
    fun getAppState(): AppState

    fun setSingleGroup(groupName: String)
    fun getSingleGroup(): SingleGroup

    fun setMultipleGroup(
        groupNameFirstPosition: String,
        groupNameSecondPosition: String,
        daysToReplace: List<Int>
    )

    fun getMultipleGroup(): MultipleGroup

    fun changeDaysToReplaceShared(daysToReplace: List<Int>)
    fun getDaysToReplaceShared(): List<Int>

    fun getSingleGroupList(): List<SingleGroup>
    fun getMultipleGroupList(): List<MultipleGroup>

    class Impl @Inject constructor(private val appConfigSetting: AppConfigContract) :
        AppConfigRepository {

        private fun changeAppState(state: AppState) {
            var appConfig = appConfigSetting.getAppConfig()
            appConfig = appConfig.copy(appState = state)
            appConfigSetting.setAppConfig(appConfig)
        }

        private fun changeToSingleGroup(singleGroup: SingleGroup) {
            var appConfig = appConfigSetting.getAppConfig()
            val list = appConfig.singleGroupConfig.list.toMutableList()
            list.add(singleGroup)
            appConfig = appConfig.copy(
                appState = AppState.SINGLE,
                singleGroupConfig = appConfig.singleGroupConfig.copy(
                    currentSingleGroup = singleGroup,
                    list = list.toList()
                )
            )
            appConfigSetting.setAppConfig(appConfig)
        }

        private fun changeToMultipleGroup(multipleGroup: MultipleGroup) {
            var appConfig = appConfigSetting.getAppConfig()
            val list = appConfig.multipleGroupConfig.list.toMutableList()
            list.add(multipleGroup)
            appConfig =
                appConfig.copy(
                    appState = AppState.MULTIPLE,
                    multipleGroupConfig = appConfig.multipleGroupConfig.copy(
                        currentMultipleGroup = multipleGroup,
                        list = list.toList()
                    )
                )
            appConfigSetting.setAppConfig(appConfig)
        }

        override fun changeAppStateToUnselect() = changeAppState(AppState.UNSELECT)
        override fun changeAppStateToSingle() = changeAppState(AppState.SINGLE)
        override fun changeAppStateToMultiple() = changeAppState(AppState.MULTIPLE)
        override fun getAppState(): AppState = appConfigSetting.getAppConfig().appState

        override fun setSingleGroup(groupName: String) = changeToSingleGroup(
            SingleGroup(
                groupName
            )
        )

        override fun getSingleGroup(): SingleGroup =
            appConfigSetting.getAppConfig().singleGroupConfig.currentSingleGroup

        override fun setMultipleGroup(
            groupNameFirstPosition: String,
            groupNameSecondPosition: String,
            daysToReplace: List<Int>
        ) = changeToMultipleGroup(
            MultipleGroup(
                groupNameFirstPosition,
                groupNameSecondPosition,
                daysToReplace
            )
        )

        override fun getMultipleGroup(): MultipleGroup =
            appConfigSetting.getAppConfig().multipleGroupConfig.currentMultipleGroup

        override fun changeDaysToReplaceShared(daysToReplace: List<Int>) {
            appConfigSetting.setAppConfig(appConfigSetting.getAppConfig().copy(multipleGroupConfig =
            appConfigSetting.getAppConfig().multipleGroupConfig.copy(daysToReplaceShared = daysToReplace)))
        }

        override fun getDaysToReplaceShared(): List<Int> = appConfigSetting.getAppConfig().multipleGroupConfig.daysToReplaceShared

        override fun getSingleGroupList(): List<SingleGroup> =
            appConfigSetting.getAppConfig().singleGroupConfig.list

        override fun getMultipleGroupList(): List<MultipleGroup> =
            appConfigSetting.getAppConfig().multipleGroupConfig.list
    }
}