package com.example.schedule.v2

import androidx.lifecycle.ViewModel
import com.example.data.repositories.ScheduleItemListRepositoryV3
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModelV2 @Inject constructor(
    private val scheduleItemListRepositoryV3: ScheduleItemListRepositoryV3,
) : ViewModel() {

    private val disposables = CompositeDisposable()


    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}