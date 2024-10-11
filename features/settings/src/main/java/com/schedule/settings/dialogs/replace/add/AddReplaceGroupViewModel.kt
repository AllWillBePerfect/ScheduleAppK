package com.schedule.settings.dialogs.replace.add

import androidx.lifecycle.ViewModel
import com.schedule.data.event_manager.RefreshEventManager
import com.schedule.data.event_manager.RestoreDialogEventManager
import com.schedule.data.repositories.v2.schedule.repository.impl.AppConfigRepositoryV2
import com.schedule.data.repositories.v2.schedule.services.WriteAndSearchListOfGroupsService
import com.schedule.models.ui.ScheduleGroupsListEntity
import com.schedule.settings.dialogs.replace.adapter.model.DayChooseItem
import com.schedule.utils.Result
import com.schedule.utils.sources.SingleEvent
import com.schedule.utils.sources.SingleLiveData
import com.schedule.utils.sources.SingleMutableLiveData
import com.schedule.views.adapter.GroupItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddReplaceGroupViewModel @Inject constructor(
    private val appConfigRepositoryV2: AppConfigRepositoryV2,
    private val groupService: WriteAndSearchListOfGroupsService,
    private val vpkService: WriteAndSearchListOfGroupsService,
    private val refreshEventManager: RefreshEventManager,
    private val restoreDialogEventManager: RestoreDialogEventManager
) : ViewModel() {

    private val _daysAdapterListLiveData = SingleMutableLiveData<List<DayChooseItem>>()
    val daysAdapterListLiveData: SingleLiveData<List<DayChooseItem>> = _daysAdapterListLiveData

    private val _adapterListLiveData = SingleMutableLiveData<EditTextEvent<List<GroupItem>>>()
    val adapterListLiveData: SingleLiveData<EditTextEvent<List<GroupItem>>> = _adapterListLiveData

    val groupLiveData = groupService.getGroupsLiveData()
    val groupFetchLiveData = groupService.getFetchScheduleLiveData()

    val vpkLiveData = vpkService.getGroupsLiveData()
    val vpkFetchLiveData = vpkService.getFetchScheduleLiveData()

    private val _resultConfigLiveData =
        SingleMutableLiveData<ResultConfig>(SingleEvent(ResultConfig()))
    val resultConfigLiveData: SingleLiveData<ResultConfig> = _resultConfigLiveData

    init {
        groupService.runTypeSubject()
        vpkService.runTypeSubject()
    }

    fun setTextToGroupLiveData(charSequence: CharSequence) = groupService.setText(charSequence)
    fun setTextToVpkLiveData(charSequence: CharSequence) = vpkService.setText(charSequence)

    fun fetchGroup(groupName: String) = groupService.fetchGroup(groupName)
    fun fetchVpk(vpkName: String) = vpkService.fetchGroup(vpkName)

    fun setAdapterListGroupIfExist() {
        groupLiveData.value?.let {
            if (it is Result.Success) {
                _adapterListLiveData.value = SingleEvent(EditTextEvent.ToGroupEditText(it.data.map {
                    GroupItem(
                        it.name,
                        it.group
                    )
                }))
            }
        }
    }

    fun setAdapterListVpkIfExist() {
        vpkLiveData.value?.let {
            if (it is Result.Success) {
                _adapterListLiveData.value = SingleEvent(EditTextEvent.ToVpkEditText(it.data.map {
                    GroupItem(
                        it.name,
                        it.group
                    )
                }))
            }
        }
    }

    fun setAdapterListGroup(list: List<ScheduleGroupsListEntity.ScheduleGroupEntity>) {
        _adapterListLiveData.value =
            SingleEvent(EditTextEvent.ToGroupEditText(list.map { GroupItem(it.name, it.group) }))
    }

    fun setAdapterListVpk(list: List<ScheduleGroupsListEntity.ScheduleGroupEntity>) {
        _adapterListLiveData.value =
            SingleEvent(EditTextEvent.ToVpkEditText(list.map { GroupItem(it.name, it.group) }))
    }

    fun setGroupNameCorrectParams(isCorrect: Boolean) {
        _resultConfigLiveData.value = SingleEvent(_resultConfigLiveData.value!!.eventForCheck!!.copy(isGroupNameCorrect = isCorrect))
    }

    fun setVpkCorrectParams(isCorrect: Boolean) {
        _resultConfigLiveData.value = SingleEvent(_resultConfigLiveData.value!!.eventForCheck!!.copy(isVpkCorrect = isCorrect))
    }

    fun setGroupNameParams(groupName: String) {
        _resultConfigLiveData.value = SingleEvent(_resultConfigLiveData.value!!.eventForCheck!!.copy(groupName = groupName, isGroupNameCorrect = true))
    }

    fun setVpkParams(vpkName: String) {
        _resultConfigLiveData.value = SingleEvent(_resultConfigLiveData.value!!.eventForCheck!!.copy(vpkName = vpkName, isVpkCorrect = true))
    }

    fun nullViewModel() {
        _resultConfigLiveData.value = SingleEvent(ResultConfig())
    }

    fun addReplaceConfig(groupName: String, vpkName: String) {
        val list = _daysAdapterListLiveData.value!!.eventForCheck!!
        val replaceDays = mutableListOf<Int>()
        list.forEachIndexed { index, innerItem ->
            if ((innerItem as DayChooseItem.ClickableChooseDay).isSelected)
                replaceDays.add(index + 1)
        }
        appConfigRepositoryV2.addReplaceGroupAndSetState(groupName, vpkName, replaceDays)
        refreshEventManager.setRefreshLiveData()
    }

    fun restoreReplaceDialog() = restoreDialogEventManager.setReplace()


    fun onItemClick(item: DayChooseItem.ClickableChooseDay) {
        val list = _daysAdapterListLiveData.value!!.eventForCheck!!
        val newList = list.map {
            if ((it as DayChooseItem.ClickableChooseDay).day == item.day)
                it.copy(isSelected = !it.isSelected)
            else it
        }
        _daysAdapterListLiveData.value = SingleEvent(newList)
    }

    fun setDaysList() {
        _daysAdapterListLiveData.value = SingleEvent(mapList(getGlobalDays()))
    }

    private fun getGlobalDays() = appConfigRepositoryV2.getReplaceGroupReplaceDays()

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

    override fun onCleared() {
        super.onCleared()
        groupService.clear()
        vpkService.clear()
    }

    sealed interface EditTextEvent<out T> {
        data class ToGroupEditText<T>(val value: T) : EditTextEvent<T>
        data class ToVpkEditText<T>(val value: T) : EditTextEvent<T>
    }

    data class ResultConfig(
        val groupName: String = "",
        val vpkName: String = "",
        val isGroupNameCorrect: Boolean = false,
        val isVpkCorrect: Boolean = false
    )

}