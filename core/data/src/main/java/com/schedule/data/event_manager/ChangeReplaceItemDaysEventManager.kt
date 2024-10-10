package com.schedule.data.event_manager

import com.schedule.utils.sources.SingleEvent
import com.schedule.utils.sources.SingleLiveData
import com.schedule.utils.sources.SingleMutableLiveData
import javax.inject.Inject

interface ChangeReplaceItemDaysEventManager {

    fun getChangeReplaceItemDays(): SingleLiveData<ReplacedDaysItem>
    fun setChangeReplaceItemDays(list: ReplacedDaysItem)

    class Impl @Inject constructor() : ChangeReplaceItemDaysEventManager {

        private val changeReplaceDaysLiveData: SingleMutableLiveData<ReplacedDaysItem> =
            SingleMutableLiveData(
                SingleEvent(null)
            )

        override fun getChangeReplaceItemDays(): SingleLiveData<ReplacedDaysItem> =
            changeReplaceDaysLiveData

        override fun setChangeReplaceItemDays(list: ReplacedDaysItem) {
            changeReplaceDaysLiveData.value = SingleEvent(list)
        }
    }

    data class ReplacedDaysItem(val groupName: String, val vpkName: String, val days: List<Int>)
}