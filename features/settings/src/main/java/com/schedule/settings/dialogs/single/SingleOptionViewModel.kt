package com.schedule.settings.dialogs.single

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.schedule.data.repositories.v2.schedule.repository.impl.AppConfigRepositoryV2
import com.schedule.data.event_manager.RefreshEventManager
import com.schedule.settings.SettingsFragmentContract
import com.schedule.settings.dialogs.adapter.model.GroupItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SingleOptionViewModel @Inject constructor(
    private val appConfigRepositoryV2: AppConfigRepositoryV2,
    private val router: SettingsFragmentContract,
    private val refreshEventManager: RefreshEventManager
) : ViewModel() {

    private val _adapterListLiveData: MutableLiveData<List<String>> = MutableLiveData()
    val adapterListLiveData: LiveData<List<String>> = _adapterListLiveData


    fun setList() {
        _adapterListLiveData.value = appConfigRepositoryV2.getSingleStorage().cachedList
    }

    fun navigate() = router.navigateToAddSingleGroupScreen()

    fun switchGroup(group: GroupItem.Single) {
        if (group.isSelected) return
        appConfigRepositoryV2.addSingleGroupAndSetState(group.groupName)
        refreshEventManager.setRefreshLiveData()
        setList()
    }

    fun deleteGroup(group: GroupItem.Single) {
        appConfigRepositoryV2.removeSingleGroup(group.groupName)
        refreshEventManager.setRefreshLiveData()
        setList()
    }

    fun isSelected(groupName: String): Boolean =
        appConfigRepositoryV2.getSingleAppStateOrNull()?.groupName == groupName

}