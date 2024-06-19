package com.example.schedule.v2

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.data.repositories.AppConfigRepository
import com.example.data.repositories.ScheduleItemListRepository
import com.example.data.repositories.ScheduleItemListRepositoryV2
import com.example.data.repositories.ScheduleItemListRepositoryV3
import com.example.utils.Result
import com.example.utils.ScheduleItem
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModelV2 @Inject constructor(
    private val appConfigRepository: AppConfigRepository,
    private val scheduleItemListRepositoryV2: ScheduleItemListRepositoryV2,
    private val scheduleItemListRepositoryV3: ScheduleItemListRepositoryV3,
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _weekViewModel = MutableLiveData<ScheduleItemListRepository.WeeksConfig>()
    val weekViewModel: LiveData<ScheduleItemListRepository.WeeksConfig> = _weekViewModel

    private val _scheduleViewModel = MutableLiveData<List<ScheduleItem>>()
    val scheduleViewModel: LiveData<List<ScheduleItem>> = _scheduleViewModel

    private val _loadingAppBarLiveData = MutableLiveData<Boolean>()
    val loadingAppBarLiveData: LiveData<Boolean> = _loadingAppBarLiveData


    init {



        /*Observable.concat(obs3, obs4)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {res ->
                    when (res) {
                        is Result.Success -> {
                            Log.d("ScheduleViewModelV2 weeksConfig success", res.data.toString())
//                            _weekViewModel.value = res.data
                            _scheduleViewModel.value = res.data
                            reuseList()
                        }

                        is Result.Error -> {
                            Log.d(
                                "ScheduleViewModelV2 weeksConfig error",
                                res.exception.toString()
                            )
                        }

                        is Result.Loading -> {
                            Log.d("ScheduleViewModelV2 weeksConfig loading", "Loading")
                        }
                    }},
                onComplete = {Log.d("ScheduleViewModelV2 weeksConfig onComplete", "onComplete") },
                onError = { Log.d("ScheduleViewModelV2 weeksConfig onError", it.message.toString())
                }
            ).addTo(disposables)*/



        /*scheduleItemListRepositoryV2.fetchWeekConfigObservable(appConfigRepository.getSingleGroup().groupName)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { res ->
                when (res) {
                    is Result.Success -> {
                        Log.d("ScheduleViewModelV2 weeksConfig success", res.data.toString())
                        _loadingAppBarLiveData.value = false
                        _weekViewModel.value = res.data
//                        reuseListResultObservable()
                        fetchScheduleObservable()
                    }

                    is Result.Error -> {
                        Log.d(
                            "ScheduleViewModelV2 weeksConfig error",
                            res.exception.message.toString()
                        )
                        _loadingAppBarLiveData.value = false
                    }

                    is Result.Loading -> {
                        Log.d("ScheduleViewModelV2 weeksConfig loading", "Loading")
                        _loadingAppBarLiveData.value = true
                    }
                }
            }.addTo(disposables)*/
    }

    fun someFun() {}

    fun reuseList() {
        scheduleItemListRepositoryV3.reuseList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {res ->
                    when (res) {
                        is Result.Success -> {
                            Log.d("ScheduleViewModelV2 reuse success", res.data.toString())
//                            _weekViewModel.value = res.data
                            _scheduleViewModel.value = res.data.list
                        }

                        is Result.Error -> {
                            Log.d(
                                "ScheduleViewModelV2 reuse error",
                                res.exception.toString()
                            )
                        }

                        is Result.Loading -> {
                            Log.d("ScheduleViewModelV2 reuse loading", "Loading")
                        }
                    }},
                onComplete = {Log.d("ScheduleViewModelV2 reuse onComplete", "onComplete")},
                onError = { Log.d("ScheduleViewModelV2 reuse onError", it.message.toString())}
            ).addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}