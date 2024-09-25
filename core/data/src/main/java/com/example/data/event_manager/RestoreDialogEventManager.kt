package com.example.data.event_manager

import com.example.utils.sources.SingleEvent
import com.example.utils.sources.SingleLiveData
import com.example.utils.sources.SingleMutableLiveData
import javax.inject.Inject

interface RestoreDialogEventManager {

    fun getRestoreDialogLiveData(): SingleLiveData<RestoreDialogState>
    fun setSingle()
    fun setReplace()
    fun setMultiple()

    class Impl @Inject constructor() : RestoreDialogEventManager {

        private val restoreDialogLiveData = SingleMutableLiveData<RestoreDialogState>()

        override fun getRestoreDialogLiveData(): SingleLiveData<RestoreDialogState> = restoreDialogLiveData
        override fun setSingle() {
            restoreDialogLiveData.value = SingleEvent(RestoreDialogState.Single)
        }

        override fun setReplace() {
            restoreDialogLiveData.value = SingleEvent(RestoreDialogState.Replace)
        }

        override fun setMultiple() {
            restoreDialogLiveData.value = SingleEvent(RestoreDialogState.Multiple)
        }

    }

    companion object {
        sealed interface RestoreDialogState {
            data object Single: RestoreDialogState
            data object Replace: RestoreDialogState
            data object Multiple: RestoreDialogState
        }
    }
}