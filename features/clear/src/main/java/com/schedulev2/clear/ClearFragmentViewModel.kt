package com.schedulev2.clear

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.schedulev2.data.repositories.ClearDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ClearFragmentViewModel @Inject constructor(
    private val clearDataRepository: ClearDataRepository
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _clearStateLiveData = MutableLiveData<ClearState>()
    val clearStateLiveData: LiveData<ClearState> = _clearStateLiveData

    fun clearData() {
        clearDataRepository.clearData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {_clearStateLiveData.value = ClearState.Loading(true) }
            .doOnDispose { Log.d("ClearFragmentViewModel doOnDispose", "dispose") }
            .subscribeBy(
                onComplete = {
                    Log.d("ClearFragmentViewModel doFinally", "complete")
                    clearDataRepository.saveChanges()
                    _clearStateLiveData.value = ClearState.Loading(false)
                    _clearStateLiveData.value = ClearState.Success
                },
                onError = { error ->
                    Log.d("ClearFragmentViewModel doOnError", "error")
                    _clearStateLiveData.value = ClearState.Loading(false)
                    _clearStateLiveData.value = ClearState.Error(error.message.toString(), error)
                }
            ).addTo(disposable)
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    sealed interface ClearState {
        data object Success : ClearState
        data class Error(val message: String, val error: Throwable): ClearState
        data class Loading(val state: Boolean): ClearState
    }
}