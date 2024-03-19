package com.example.enter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.enter.retrofit.RetrofitFactory
import com.example.enter.retrofit.models.ScheduleGroupsListDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import javax.inject.Inject

@HiltViewModel
class EnterViewModel @Inject constructor() : ViewModel() {

    private val scheduleApi = RetrofitFactory.getScheduleApi()
    val disposables = CompositeDisposable()

    private val editTextSubject = BehaviorSubject.create<CharSequence>()
    fun editTextSet(charSequence: CharSequence) {
        editTextSubject.onNext(charSequence)
    }

    private val _groupsLiveData = MutableLiveData<List<ScheduleGroupsListDTO.ScheduleGroupDTO>>(
        emptyList()
    )
    val groupsLiveData: LiveData<List<ScheduleGroupsListDTO.ScheduleGroupDTO>> = _groupsLiveData

    init {
        editTextSubject
            .share()
            .debounce(450L, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .flatMap { scheduleApi.fetchGroupListObservable(it.toString()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .subscribeBy(
                onNext = {
                    Log.d("EnterViewModel", it.toString())
//                    if (it.choices.isNullOrEmpty()) throw ApiExceptions.HtmExists()
                    if (it.choices.isNullOrEmpty())
                        _groupsLiveData.value = groupsLiveData.value ?: emptyList()
                    else
                        _groupsLiveData.value = it.choices.take(8).filter { item -> true }

                },
                onError = {
                    Log.d("EnterViewModel", it.toString())
                },
                onComplete = { Log.d("EnterViewModel", "complete") }
            ).addTo(disposables)



    }

    fun someFun() {}

    sealed class ApiExceptions : Throwable() {
        class HtmExists : ApiExceptions()
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}