package com.schedule.data.repositories.v2.schedule.repository.impl

import android.util.Log
import com.schedule.models.sharpref.v2.AppConfigV2
import com.schedule.models.sharpref.v2.AppStateV2
import com.schedule.models.sharpref.v2.MultipleStorage
import com.schedule.models.sharpref.v2.ReplaceStorage
import com.schedule.models.sharpref.v2.SingleStorage
import com.schedule.sharpref.sources.v2.AppConfigV2Contract
import javax.inject.Inject

interface AppConfigRepositoryV2 {

    fun isAuthorized(): Boolean
    fun getAppState(): AppStateV2

    //    fun addSingleGroup(groupName: String)
    fun addSingleGroupAndSetState(groupName: String)
    fun removeSingleGroup(groupName: String)
    fun authorize(groupName: String)

    fun addMultipleGroupAndSetState(groupName: String)
    fun removeMultipleGroup(groupName: String)
    fun moveMultipleGroup(list: List<String>)
    fun selectMultipleGroup()
    fun selectMultipleGroupAndSetState()

    fun getReplaceGroupReplaceDays(): List<Int>
    fun changeReplaceGroupReplaceDays(list: List<Int>)
    fun findAndChangeReplaceGroupReplaceDays(groupName: String, vpkName: String, list: List<Int>)
    fun changeReplaceGroupIsGlobalState(isGlobal: Boolean)

    fun addReplaceGroupAndSetState(groupName: String, vpkName: String, replaceDays: List<Int>)
    fun removeReplaceGroup(groupName: String, vpkName: String)

    fun getTitle(): String

    fun getSingleAppState(): AppStateV2.Single
    fun getSingleAppStateOrNull(): AppStateV2.Single?
    fun getSingleStorage(): SingleStorage

    fun getMultipleAppState(): AppStateV2.Multiple
    fun getMultipleAppStateOrNull(): AppStateV2.Multiple?
    fun getMultipleStorage(): MultipleStorage

    fun getReplaceAppState(): AppStateV2.Replace
    fun getReplaceAppStateOrNull(): AppStateV2.Replace?
    fun getReplaceStorage(): ReplaceStorage

    class Impl @Inject constructor(
        private val appConfigV2Contract: AppConfigV2Contract
    ) : AppConfigRepositoryV2 {


        override fun isAuthorized(): Boolean =
            appConfigV2Contract.getAppConfigV2().currentState !is AppStateV2.Unselected

        override fun getAppState(): AppStateV2 = appConfigV2Contract.getAppConfigV2().currentState

        override fun addSingleGroupAndSetState(groupName: String) {
            addSingleGroup(groupName)
            setSingleConfig(groupName)
        }

        override fun removeSingleGroup(groupName: String) {
            removeSingleGroupp(groupName)
        }


        override fun authorize(groupName: String) {
            addSingleGroup(groupName)
            setSingleConfig(groupName)
        }

        override fun addMultipleGroupAndSetState(groupName: String) {
            addMultipleGroup(groupName)
            setMultiConfig(getMultipleStorage().cachedList)
        }

        override fun removeMultipleGroup(groupName: String) {
            removeMultipleGroupp(groupName)
        }

        override fun moveMultipleGroup(list: List<String>) {
            moveMultipleGroupp(list)
        }

        override fun selectMultipleGroup() {
            appConfigV2Contract.setMultipleStorage(getMultipleStorage())
        }

        override fun selectMultipleGroupAndSetState() {
            val list = getMultipleStorage().cachedList
            if (list.isEmpty()) return
            setMultiConfig(getMultipleStorage().cachedList)
        }

        override fun getReplaceGroupReplaceDays(): List<Int> =
            appConfigV2Contract.getReplaceStorage().cachedGlobalReplacedDays

        override fun changeReplaceGroupReplaceDays(list: List<Int>) {
            val replaceStorage = getReplaceStorage()
            appConfigV2Contract.setReplaceStorage(replaceStorage.copy(cachedGlobalReplacedDays = list))
        }

        override fun findAndChangeReplaceGroupReplaceDays(
            groupName: String,
            vpkName: String,
            list: List<Int>
        ) {
            val replaceStorage = appConfigV2Contract.getReplaceStorage()
            val listt = replaceStorage.cachedList.toMutableList()
            val item = listt.find {it.groupName == groupName && it.vpkName == vpkName}
            val index = listt.indexOfFirst { it.groupName == groupName && it.vpkName == vpkName }
            if (index == -1 || item == null) return
            listt[index] = item.copy(replaceDays = list)
            appConfigV2Contract.setReplaceStorage(
                replaceStorage.copy(cachedList = listt)
            )
            val state = getAppState()
            if (state is AppStateV2.Replace) {
                if (item.groupName == state.groupName && item.vpkName == state.vpkName)
                    setReplaceConfig(groupName, vpkName, list)
            }
        }


        override fun changeReplaceGroupIsGlobalState(isGlobal: Boolean) {
            appConfigV2Contract.setReplaceStorage(getReplaceStorage().copy(isGlobal = isGlobal))
        }

        override fun addReplaceGroupAndSetState(
            groupName: String,
            vpkName: String,
            replaceDays: List<Int>
        ) {
            addReplaceGroup(groupName, vpkName, replaceDays)
            setReplaceConfig(groupName, vpkName, replaceDays)
        }

        override fun removeReplaceGroup(groupName: String, vpkName: String) {
            removeReplaceGroupp(groupName, vpkName)
        }

        override fun getTitle(): String {
            return when (val state = appConfigV2Contract.getAppConfigV2().currentState) {
                is AppStateV2.Single -> state.groupName
                is AppStateV2.Replace -> "${state.groupName}/${state.vpkName}"
                is AppStateV2.Multiple -> state.groupNames.joinToString("/")
                is AppStateV2.Unselected -> "Unselected"
            }
        }

        override fun getSingleAppState(): AppStateV2.Single =
            appConfigV2Contract.getAppConfigV2().currentState as AppStateV2.Single

        override fun getSingleAppStateOrNull(): AppStateV2.Single? {
            val state = appConfigV2Contract.getAppConfigV2().currentState
            return if (state is AppStateV2.Single) state else null
        }

        override fun getSingleStorage(): SingleStorage = appConfigV2Contract.getSingleStorage()
        override fun getMultipleAppState(): AppStateV2.Multiple =
            appConfigV2Contract.getAppConfigV2().currentState as AppStateV2.Multiple

        override fun getMultipleAppStateOrNull(): AppStateV2.Multiple? =
            appConfigV2Contract.getAppConfigV2().currentState as? AppStateV2.Multiple

        override fun getMultipleStorage(): MultipleStorage =
            appConfigV2Contract.getMultipleStorage()

        override fun getReplaceAppState(): AppStateV2.Replace {
            val state = appConfigV2Contract.getAppConfigV2().currentState as AppStateV2.Replace
            val replaceStorage = getReplaceStorage()
            return state.copy(replaceDays = if (replaceStorage.isGlobal) replaceStorage.cachedGlobalReplacedDays else state.replaceDays)
        }

        override fun getReplaceAppStateOrNull(): AppStateV2.Replace? {
            val state = appConfigV2Contract.getAppConfigV2().currentState as? AppStateV2.Replace
            val replaceStorage = getReplaceStorage()
            return state?.copy(replaceDays = if (replaceStorage.isGlobal) replaceStorage.cachedGlobalReplacedDays else state.replaceDays)
        }

        override fun getReplaceStorage(): ReplaceStorage = appConfigV2Contract.getReplaceStorage()

        private fun addSingleGroup(groupName: String) {
            val singleStorage = appConfigV2Contract.getSingleStorage()
            val list = singleStorage.cachedList.toMutableList()
            if (list.contains(groupName)) return
            list.add(groupName)
            appConfigV2Contract.setSingleStorage(
                singleStorage.copy(cachedList = list)
            )
        }

        private fun removeSingleGroupp(groupName: String) {
            val singleStorage = appConfigV2Contract.getSingleStorage()
            val list = singleStorage.cachedList.toMutableList()
            val state = getSingleAppStateOrNull()
            if (state?.groupName == groupName) return
            list.remove(groupName)
            appConfigV2Contract.setSingleStorage(
                singleStorage.copy(cachedList = list)
            )
        }



        private fun addMultipleGroup(groupName: String) {
            val multipleStorage = appConfigV2Contract.getMultipleStorage()
            val list = multipleStorage.cachedList.toMutableList()
            addSingleGroup(groupName)
            if (list.contains(groupName)) return
            list.add(groupName)
            appConfigV2Contract.setMultipleStorage(
                multipleStorage.copy(cachedList = list)
            )
        }

        private fun removeMultipleGroupp(groupName: String) {
            val multipleStorage = appConfigV2Contract.getMultipleStorage()
            val list = multipleStorage.cachedList.toMutableList()
            val state = getMultipleAppStateOrNull()
            if (state != null && list.size == 1) return
            list.remove(groupName)
            appConfigV2Contract.setMultipleStorage(
                multipleStorage.copy(cachedList = list)
            )
        }

        private fun removeReplaceGroupp(groupName: String, vpkName: String) {
            val replaceStorage = appConfigV2Contract.getReplaceStorage()
            val list = replaceStorage.cachedList.toMutableList()
            val state = getReplaceAppStateOrNull()
            if (state?.groupName == groupName && state.vpkName == vpkName) return
            val item = list.find {it.groupName == groupName && it.vpkName == vpkName}
            list.remove(item)
            appConfigV2Contract.setReplaceStorage(
                replaceStorage.copy(cachedList = list)
            )
        }

        private fun moveMultipleGroupp(list: List<String>) {
            val multipleStorage = appConfigV2Contract.getMultipleStorage()
            appConfigV2Contract.setMultipleStorage(
                multipleStorage.copy(cachedList = list)
            )
        }

        private fun addReplaceGroup(groupName: String, vpkName: String, replaceDays: List<Int>) {
            val replaceStore = appConfigV2Contract.getReplaceStorage()
            val list = replaceStore.cachedList.toMutableList()
            addSingleGroup(groupName)
            addSingleGroup(vpkName)
            if (list.any { it.groupName == groupName && it.vpkName == vpkName }) return
            list.add(ReplaceStorage.ReplaceItem(groupName, vpkName, replaceDays))
            appConfigV2Contract.setReplaceStorage(
                replaceStore.copy(cachedList = list)
            )
        }

        private fun setSingleConfig(groupName: String) {
//            val appConfig = appConfigV2Contract.getAppConfigV2().copy(
//                currentState = AppStateV2.Single(groupName)
//            )
            appConfigV2Contract.setAppConfigV2(
                AppConfigV2(
                    currentState = AppStateV2.Single(
                        groupName
                    )
                )
            )
        }

        private fun setReplaceConfig(groupName: String, vpkName: String, replaceDays: List<Int>) {
//            val appConfig = appConfigV2Contract.getAppConfigV2().copy(
//                currentState = AppStateV2.Replace(
//                    groupName = groupName,
//                    vpkName = vpkName,
//                    replaceDays = replaceDays
//                )
//            )
            appConfigV2Contract.setAppConfigV2(
                AppConfigV2(
                    currentState = AppStateV2.Replace(
                        groupName = groupName,
                        vpkName = vpkName,
                        replaceDays = replaceDays
                    )
                )
            )
        }

        private fun setMultiConfig(groupNames: List<String>) {
//            val appConfig = appConfigV2Contract.getAppConfigV2().copy(
//                currentState = AppStateV2.Multiple(groupNames)
//            )
            appConfigV2Contract.setAppConfigV2(
                AppConfigV2(
                    currentState = AppStateV2.Multiple(
                        groupNames = groupNames
                    )
                )
            )
        }
    }
}