package com.example.schedule.v2.search

import androidx.lifecycle.ViewModel
import com.example.data.repositories.v2.WriteAndSearchListOfGroupsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val writeAndSearchListOfGroupsRepository: WriteAndSearchListOfGroupsRepository
): ViewModel() {

    val groupLiveData = writeAndSearchListOfGroupsRepository.getGroupsLiveData()
    val fetchLiveData = writeAndSearchListOfGroupsRepository.getFetchScheduleLiveData()

    init {
        writeAndSearchListOfGroupsRepository.runTypeSubject()
    }

    fun setText(charSequence: CharSequence) = writeAndSearchListOfGroupsRepository.setText(charSequence)
    fun fetchGroup(groupName: String) = writeAndSearchListOfGroupsRepository.fetchGroup(groupName)

    override fun onCleared() {
        super.onCleared()
        writeAndSearchListOfGroupsRepository.clear()
    }
}