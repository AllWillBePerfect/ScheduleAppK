package com.schedule.settings.modal

import android.util.Log
import androidx.lifecycle.ViewModel
import com.schedule.data.event_manager.ChangeReplaceItemDaysEventManager
import com.schedule.data.event_manager.RefreshEventManager
import com.schedule.data.repositories.v2.schedule.repository.impl.AppConfigRepositoryV2
import com.schedule.settings.dialogs.replace.adapter.model.DayChooseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeDaysModalViewModel @Inject constructor(
    private val appConfigRepositoryV2: AppConfigRepositoryV2,
    private val refreshEventManager: RefreshEventManager,
    private val changeReplaceItemDaysEventManager: ChangeReplaceItemDaysEventManager
) : ViewModel() {

    val liveData = changeReplaceItemDaysEventManager.getChangeReplaceItemDays()

    fun onDayClick(item: DayChooseItem.ClickableChooseDay) {
        val list = mapList(liveData.value!!.eventForCheck!!.days)
        val newList = list.map {
            if (it.day == item.day)
                it.copy(isSelected = !it.isSelected)
            else it
        }
        val intList = mutableListOf<Int>()
        for (i in 1..newList.size) {
            if (newList[i - 1].isSelected) {
                intList.add(i)
            }
        }
        changeReplaceItemDaysEventManager.setChangeReplaceItemDays(liveData.value!!.eventForCheck!!.copy(days = intList))
    }

    fun saveChanges() {

        val item = liveData.value!!.eventForCheck!!
        appConfigRepositoryV2.findAndChangeReplaceGroupReplaceDays(item.groupName, item.vpkName, item.days)
        refreshEventManager.setRefreshLiveData()
    }

    fun mapList(list: List<Int>): List<DayChooseItem.ClickableChooseDay> {
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