package com.schedule.settings.dialogs.replace

import androidx.lifecycle.ViewModel
import com.schedule.data.event_manager.ChangeReplaceItemDaysEventManager
import com.schedule.data.event_manager.RefreshEventManager
import com.schedule.data.repositories.v2.schedule.repository.impl.AppConfigRepositoryV2
import com.schedule.models.sharpref.v2.ReplaceStorage
import com.schedule.settings.SettingsFragmentContract
import com.schedule.settings.dialogs.adapter.model.GroupItem
import com.schedule.settings.dialogs.replace.adapter.model.DayChooseItem
import com.schedule.utils.sources.SingleEvent
import com.schedule.utils.sources.SingleLiveData
import com.schedule.utils.sources.SingleMutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReplaceOptionViewModel @Inject constructor(
    private val appConfigRepositoryV2: AppConfigRepositoryV2,
    private val refreshEventManager: RefreshEventManager,
    private val changeReplaceItemDaysEventManager: ChangeReplaceItemDaysEventManager,
    private val router: SettingsFragmentContract
) : ViewModel() {

    private val _replaceDaysLiveData = SingleMutableLiveData<List<DayChooseItem>>()
    val replaceDaysLiveData: SingleLiveData<List<DayChooseItem>> = _replaceDaysLiveData
    fun setReplaceDaysLiveData() {
        _replaceDaysLiveData.value = SingleEvent(mapList(getGlobalDays()))
    }

    private val _updateUiLiveData = SingleMutableLiveData<ReplaceUiConfig>()
    val updateUiLiveData: SingleLiveData<ReplaceUiConfig> = _updateUiLiveData
    fun setList() {
        _updateUiLiveData.value =
            SingleEvent(mapToUiList(appConfigRepositoryV2.getReplaceStorage().cachedList))
    }

    val refreshLiveData = refreshEventManager.getRefreshLiveData()

    private fun mapToUiList(list: List<ReplaceStorage.ReplaceItem>): ReplaceUiConfig {
        val state = appConfigRepositoryV2.getReplaceStorage().isGlobal
        return ReplaceUiConfig(
            isShared = state,
            adapterList = list.map {
                GroupItem.Replace(
                    groupName = it.groupName,
                    vpkName = it.vpkName,
                    replacedDays = it.replaceDays,
                    isSelected = isSelected(it.groupName, it.vpkName),
                    isShowDays = !state
                )
            }
        )
    }


    data class ReplaceUiConfig(
        val isShared: Boolean,
        val adapterList: List<GroupItem>
    )

//    fun setAdapter() {
//        val state = appConfigRepositoryV2.getReplaceStorage().isGlobal
//        _updateUiLiveData.value = SingleEvent(
//            ReplaceUiConfig(
//                isShared = state,
//                adapterList = createAdapterList(!state)
//            )
//        )
//    }

    fun getIsGlobalState() = appConfigRepositoryV2.getReplaceStorage().isGlobal
    fun changeIsGlobalState(isGlobal: Boolean) {
        appConfigRepositoryV2.changeReplaceGroupIsGlobalState(isGlobal)
        setList()
        refreshEventManager.setRefreshLiveData()
    }

    private fun getGlobalDays() = appConfigRepositoryV2.getReplaceGroupReplaceDays()
    private fun changeGlobalDays(list: List<Int>) {
        appConfigRepositoryV2.changeReplaceGroupReplaceDays(list)
        refreshEventManager.setRefreshLiveData()
    }

    fun onDayClick(item: DayChooseItem.ClickableChooseDay) {
        val list = _replaceDaysLiveData.value!!.eventForCheck!!
        val newList = list.map {
            if ((it as DayChooseItem.ClickableChooseDay).day == item.day)
                it.copy(isSelected = !it.isSelected)
            else it
        }
        _replaceDaysLiveData.value = SingleEvent(newList)
        val selectedDays = mutableListOf<Int>()
        newList.forEachIndexed { index, innerItem ->
            if (innerItem.isSelected)
                selectedDays.add(index + 1)
        }
        changeGlobalDays(selectedDays)
    }

    fun switchGroup(group: GroupItem.Replace) {
        if (group.isSelected) return
        appConfigRepositoryV2.addReplaceGroupAndSetState(
            groupName = group.groupName,
            vpkName = group.vpkName,
            replaceDays = group.replacedDays
        )
        refreshEventManager.setRefreshLiveData()
        setReplaceDaysLiveData()
        setList()
    }

    fun deleteGroup(replace: GroupItem.Replace) {
        appConfigRepositoryV2.removeReplaceGroup(replace.groupName, replace.vpkName)
        refreshEventManager.setRefreshLiveData()
        setList()
    }

    fun changeGroupReplacedDays(replace: GroupItem.Replace) {
        changeReplaceItemDaysEventManager.setChangeReplaceItemDays(
            ChangeReplaceItemDaysEventManager.ReplacedDaysItem(
                replace.groupName,
                replace.vpkName,
                replace.replacedDays
            )
        )
        router.launchChangeModal()
    }

    fun isSelected(groupName: String, vpkName: String): Boolean {
        val appState = appConfigRepositoryV2.getReplaceAppStateOrNull()
        appState?.let { return it.groupName == groupName && it.vpkName == vpkName }
        return false
    }

    fun navigate() = router.navigateToAddReplaceGroupScreen()

    private fun createAdapterList(isShowDays: Boolean) = listOf(
        GroupItem.Replace(
            groupName = "КТбо4-2",
            vpkName = "ВПК 1-8",
            replacedDays = listOf(1, 5),
            isSelected = isSelected("КТбо4-2", "ВПК 1-8"),
            isShowDays = isShowDays
        ),
        GroupItem.Replace(
            groupName = "КТбо4-5",
            vpkName = "ВПК 3-8",
            replacedDays = listOf(1, 5),
            isSelected = isSelected("КТбо4-5", "ВПК 3-8"),
            isShowDays = isShowDays
        )
    )

    private fun mapList(list: List<Int>): List<DayChooseItem.ClickableChooseDay> {
        val listItems: MutableList<DayChooseItem.ClickableChooseDay> = mutableListOf()
        for (i in 1..6) {
            listItems.add(DayChooseItem.ClickableChooseDay(mapIntToString(i), list.contains(i)))
        }
        return listItems
    }

    private fun mapIntToString(value: Int): String {
        return when (value) {
            1 -> "ПН"
            2 -> "ВТ"
            3 -> "СР"
            4 -> "ЧТ"
            5 -> "ПТ"
            6 -> "СБ"
            else -> throw IllegalArgumentException("Wrong day")
        }
    }


}