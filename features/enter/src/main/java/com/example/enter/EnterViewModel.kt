package com.example.enter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.data.repositories.AppConfigRepository
import com.example.data.repositories.ScheduleApiRepository
import com.example.enter.adapter.GroupItem
import com.example.models.ui.ScheduleEntity
import com.example.models.ui.ScheduleGroupsListEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import javax.inject.Inject

@HiltViewModel
class EnterViewModel @Inject constructor(
    private val appConfigRepository: AppConfigRepository,
    private val scheduleApiRepository: ScheduleApiRepository
) : ViewModel() {

    private val scheduleApi = scheduleApiRepository
    val disposables = CompositeDisposable()

    private val editTextSubject = PublishSubject.create<CharSequence>()
    private fun getEditTextSubject(): Observable<CharSequence> = editTextSubject.hide().share()
    fun editTextSet(charSequence: CharSequence) {
        editTextSubject.onNext(charSequence)
    }

    private val groupsSubject =
        BehaviorSubject.create<List<ScheduleGroupsListEntity.ScheduleGroupEntity>>()

    private fun getGroupsSubject(): Observable<List<ScheduleGroupsListEntity.ScheduleGroupEntity>> =
        groupsSubject.hide().share()

    private val _groupsLiveData = MutableLiveData<List<GroupItem>>(
        emptyList()
    )
    val groupsLiveData: LiveData<List<GroupItem>> = _groupsLiveData

    private val _loadingAppBarLiveData = MutableLiveData<Boolean>()
    val loadingAppBarLiveData: LiveData<Boolean> = _loadingAppBarLiveData

    private val _loadingProgressBarLiveData = MutableLiveData<Boolean>()
    val loadingProgressBarLiveData: LiveData<Boolean> = _loadingProgressBarLiveData

    init {
        getEditTextSubject()
            .map {
                _loadingAppBarLiveData.postValue(true)
                it
            }
            .filter {
                val value = it.isNotEmpty()
                if (!value) _loadingAppBarLiveData.postValue(false)
                value
            }
            .debounceIf({ it.length > 1 }, 450L, TimeUnit.MILLISECONDS)
//            .distinctUntilChanged()
            .observeOn(Schedulers.io()).switchMap {
                scheduleApi.fetchGroupListObservable(it.toString())
                    /*.onErrorResumeNext(
                        Observable.just(
                            ScheduleGroupsListEntity(
                                emptyList()
                            )
                        )
                    )*/
                    .onErrorReturn {
                        if (it is UnknownHostException)
                            ScheduleGroupsListEntity(
                                emptyList()
                            )
                        else
                            ScheduleGroupsListEntity(
                                emptyList()
                            )
                    }
            }
            .doOnDispose { Log.d("EnterViewModel", "dispose") }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                Log.d("EnterViewModel", it.toString())
//                    if (it.choices.isNullOrEmpty()) throw ApiExceptions.HtmExists()
                _loadingAppBarLiveData.postValue(false)
                if (it.choices.isNullOrEmpty()) {
                    groupsSubject.onNext(groupsSubject.value ?: emptyList())
                } else {
                    groupsSubject.onNext(it.choices)
                }
            }, onError = {
//                    throw Exception(it)
                Log.d("EnterViewModel error", it.toString())
            }, onComplete = { Log.d("EnterViewModel", "complete") }).addTo(disposables)

        Observables.combineLatest(getEditTextSubject(), getGroupsSubject()) { text, groups ->
            Log.d("EnterViewModel text combine", text.toString())
            Log.d("EnterViewModel group combine", groups.toString())
            groups.stream().filter { group ->
                group.name.uppercase().contains(text.toString().uppercase())
            }.limit(8).collect(Collectors.toList())
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                Log.d("EnterViewModel result", list.toString())
                _groupsLiveData.value = list.map { GroupItem(it.name, it.group) }
            }.addTo(disposables)
        Log.d("EnterViewModel Config", appConfigRepository.getAppState().name)


    }

    fun fetchGroup(groupName: String) {
        _loadingProgressBarLiveData.value = true
        Observable.just(groupName)
            .switchMap {
                scheduleApiRepository.fetchScheduleByGroupNameObservable(groupName)
                    .onErrorResumeNext(htmObservable(groupName))
            }
            .doOnDispose { Log.d("EnterViewModel dispose", "dispose") }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    Log.d("EnterViewModel fetch", it.toString())
                    _loadingProgressBarLiveData.value = false
                    appConfigRepository.setSingleGroup(it.name)
                },
                onError = {
                    if (it is UnknownHostException)
                        Log.d("EnterViewModel error", "No network connection")
                    else
                        Log.d("EnterViewModel error", it.toString())
                    _loadingProgressBarLiveData.value = false
                },
                onComplete = {
                    Log.d("EnterViewModel complete", "fetch Completed")
                    _loadingProgressBarLiveData.value = false
                }
            ).addTo(disposables)


    }

    private fun htmObservable(groupName: String): Observable<ScheduleEntity> =
        Observable
            .just(groupName)
            .flatMap { scheduleApiRepository.fetchGroupListObservable(it) }
            .flatMap { scheduleApiRepository.fetchScheduleByHtmlObservable(it.choices.first().group) }


    private fun <T> Observable<T>.debounceIf(
        predicate: (T) -> Boolean,
        timeout: Long,
        unit: TimeUnit,
    ): Observable<T> {
        return this.publish { sharedSrc ->
            Observable.merge(sharedSrc.debounce(timeout, unit).filter { predicate(it) },
                sharedSrc.filter { !predicate(it) })
        }
    }

    sealed class ApiExceptions : Throwable() {
        class HtmExists : ApiExceptions()
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}