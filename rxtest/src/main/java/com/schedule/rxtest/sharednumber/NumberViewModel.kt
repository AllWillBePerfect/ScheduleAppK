package com.schedule.rxtest.sharednumber

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class NumberViewModel @Inject constructor() : ViewModel() {

    private val disposables = CompositeDisposable()
    private val numbersSubject: BehaviorSubject<MutableList<Int>> =
        BehaviorSubject.createDefault<MutableList<Int>>(mutableListOf())
    private val selectedNumbers = MutableLiveData<List<Int>>()

    fun getSelectedNumbers(): MutableLiveData<List<Int>> = selectedNumbers

    init {
        numbersSubject.subscribeBy { numbers ->
            selectedNumbers.value = numbers
            Log.d("NumberViewModel", numbers.toString())
        }.addTo(disposables)
    }

    fun addNumber(number: Int) {
        numbersSubject.value?.add(number)
        numbersSubject.onNext(numbersSubject.value!!)
    }

    fun clearNumber() {
        numbersSubject.value?.clear()
        numbersSubject.onNext(numbersSubject.value!!)
    }

    fun subscribeSelectedNumbers(selectedPhotos: Observable<Int>) {
        selectedPhotos
            .doOnComplete {
                Log.v("SharedViewModel", "Completed selecting numbers")
            }
            .subscribe { photo ->
                numbersSubject.value?.add(photo)
                numbersSubject.onNext(numbersSubject.value!!)
            }
            .addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}