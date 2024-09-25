package com.schedule.schedule.v2.search

import androidx.lifecycle.ViewModel
import com.schedule.data.repositories.v2.schedule.repository.impl.AppConfigRepositoryV2
import com.schedule.data.repositories.v2.schedule.services.WriteAndSearchListOfGroupsService
import com.schedule.data.event_manager.RefreshEventManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val writeAndSearchListOfGroupsService: WriteAndSearchListOfGroupsService,
    private val appConfigRepositoryV2: AppConfigRepositoryV2,
    private val refreshEventManager: RefreshEventManager

) : ViewModel() {

    val groupLiveData = writeAndSearchListOfGroupsService.getGroupsLiveData()
    val fetchLiveData = writeAndSearchListOfGroupsService.getFetchScheduleLiveData()

    init {
        writeAndSearchListOfGroupsService.runTypeSubject()
    }

    fun setText(charSequence: CharSequence) =
        writeAndSearchListOfGroupsService.setText(charSequence)

    fun fetchGroup(groupName: String) = writeAndSearchListOfGroupsService.fetchGroup(groupName)

    fun setSingleConfig(groupName: String) {
        appConfigRepositoryV2.addSingleGroupAndSetState(groupName)
        refreshEventManager.setRefreshLiveData()
    }

    override fun onCleared() {
        super.onCleared()
        writeAndSearchListOfGroupsService.clear()
    }
}