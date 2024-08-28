package com.example.schedule.v2

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.data.repositories.ScheduleItemListRepositoryV3
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModelV2 @Inject constructor(
) : ViewModel() {

    private val disposables = CompositeDisposable()

    init {
        Log.d("ScheduleViewModelV2", "init")
    }


    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}