package com.schedulev2.settings.dialogs.multiple

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.schedulev2.data.repositories.v2.schedule.repository.impl.AppConfigRepositoryV2
import com.schedulev2.data.event_manager.RefreshEventManager
import com.schedulev2.settings.SettingsFragmentContract
import com.schedulev2.settings.dialogs.adapter.model.GroupItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MultipleOptionViewModel @Inject constructor(
    private val appConfigRepositoryV2: AppConfigRepositoryV2,
    private val router: SettingsFragmentContract,
    private val refreshEventManager: RefreshEventManager
) : ViewModel() {

    private val _adapterListLiveData: MutableLiveData<List<String>> = MutableLiveData()
    val adapterListLiveData: LiveData<List<String>> = _adapterListLiveData

    fun setList() {
        _adapterListLiveData.value = appConfigRepositoryV2.getMultipleStorage().cachedList
    }

    fun switchGroup() {
        appConfigRepositoryV2.selectMultipleGroupAndSetState()
        refreshEventManager.setRefreshLiveData()
        setList()
    }

    fun deleteGroup(multiple: GroupItem.Multiple) {
        appConfigRepositoryV2.removeMultipleGroup(multiple.groupName)
        refreshEventManager.setRefreshLiveData()
        setList()
    }

    fun moveGroups(list: List<GroupItem.Multiple>) {
        appConfigRepositoryV2.moveMultipleGroup(list.map { it.groupName})
        appConfigRepositoryV2.selectMultipleGroup()
        refreshEventManager.setRefreshLiveData()
        setList()
    }

    fun isSelected(): Boolean =
        appConfigRepositoryV2.getMultipleAppStateOrNull() != null

    fun navigate() = router.navigateToAddMultipleGroupScreen()


}