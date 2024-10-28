package com.schedulev2.enter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.schedulev2.data.repositories.AppConfigRepository
import com.schedulev2.data.repositories.ScheduleApiRepository
import com.schedulev2.data.repositories.v2.schedule.repository.impl.AppConfigRepositoryV2
import com.schedulev2.data.event_manager.RefreshEventManager
import com.schedulev2.data.event_manager.RestoreDialogEventManager
import com.schedulev2.views.adapter.GroupItem
import com.schedulev2.models.ui.ScheduleEntity
import com.schedulev2.models.ui.ScheduleGroupsListEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import javax.inject.Inject

@HiltViewModel
class EnterViewModel @Inject constructor(
    private val appConfigRepository: AppConfigRepository,
    private val appConfigRepositoryV2: AppConfigRepositoryV2,
    private val scheduleApiRepository: ScheduleApiRepository,
    private val refreshEventManager: RefreshEventManager,
    private val restoreDialogEventManager: RestoreDialogEventManager
) : ViewModel() {

    val defaultSearchValue = "КТбо1"

    private val scheduleApi = scheduleApiRepository
    val disposables = CompositeDisposable()

    private val editTextSubject = BehaviorSubject.createDefault<CharSequence>(defaultSearchValue)
    private fun getEditTextSubject(): Observable<CharSequence> = editTextSubject.hide().share()
    fun editTextSet(charSequence: CharSequence) {
        editTextSubject.onNext(charSequence)
    }

    private val groupsSubject =
        BehaviorSubject.create<List<ScheduleGroupsListEntity.ScheduleGroupEntity>>()

    private fun getGroupsSubject(): Observable<List<ScheduleGroupsListEntity.ScheduleGroupEntity>> =
        groupsSubject.hide().share()

    private val _groupsLiveData = MutableLiveData<List<GroupItem>>(emptyList())
    val groupsLiveData: LiveData<List<GroupItem>> = _groupsLiveData

    private val _fetchScheduleLiveData = MutableLiveData<FetchResult>()
    val fetchScheduleLiveData: LiveData<FetchResult> = _fetchScheduleLiveData

    private val _loadingAppBarLiveData = MutableLiveData<Boolean>()
    val loadingAppBarLiveData: LiveData<Boolean> = _loadingAppBarLiveData

    init {
        getEditTextSubject()
            .map {
                _loadingAppBarLiveData.postValue(true)
                it.ifEmpty { defaultSearchValue }
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
                    .map { it.copy(choices = it.choices.sortedBy { it.name }) }
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

    fun fetchGroupSingle(groupName: String) {
        _fetchScheduleLiveData.value = FetchResult.Loading(true)
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
                    _fetchScheduleLiveData.value = FetchResult.Loading(false)
                    appConfigRepository.setSingleGroup(it.name)
                    appConfigRepositoryV2.authorize(it.name)
                    _fetchScheduleLiveData.value = FetchResult.Success(it)
                },
                onError = {
                    if (it is UnknownHostException)
                        Log.d("EnterViewModel error", "No network connection")
                    else
                        Log.d("EnterViewModel error", it.toString())
                    _fetchScheduleLiveData.value = FetchResult.Loading(false)
                    _fetchScheduleLiveData.value = FetchResult.Error(it.message.toString(), it)
                },
                onComplete = {
                    Log.d("EnterViewModel complete", "fetch Completed")
                    _fetchScheduleLiveData.value = FetchResult.Loading(false)
                }
            ).addTo(disposables)


    }

    fun fetchGroupMultiple(groupName: String) {
        _fetchScheduleLiveData.value = FetchResult.Loading(true)
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
                    _fetchScheduleLiveData.value = FetchResult.Loading(false)
                    appConfigRepository.setSingleGroup(it.name)
                    appConfigRepositoryV2.addMultipleGroupAndSetState(it.name)
                    _fetchScheduleLiveData.value = FetchResult.Success(it)
                },
                onError = {
                    if (it is UnknownHostException)
                        Log.d("EnterViewModel error", "No network connection")
                    else
                        Log.d("EnterViewModel error", it.toString())
                    _fetchScheduleLiveData.value = FetchResult.Loading(false)
                    _fetchScheduleLiveData.value = FetchResult.Error(it.message.toString(), it)
                },
                onComplete = {
                    Log.d("EnterViewModel complete", "fetch Completed")
                    _fetchScheduleLiveData.value = FetchResult.Loading(false)
                }
            ).addTo(disposables)


    }

    private fun htmObservable(groupName: String): Observable<ScheduleEntity> =
        Observable
            .just(groupName)
            .flatMap { scheduleApiRepository.fetchGroupListObservable(it) }
            .map { it.copy(choices = it.choices.sortedBy { it.name }) }
            .flatMap { scheduleApiRepository.fetchScheduleByHtmlObservable((it.choices.find { it.name == groupName } ?: it.choices.first()).group) }

    fun setRefreshLiveData() = refreshEventManager.setRefreshLiveData()
    fun restoreSingleDialog() = restoreDialogEventManager.setSingle()
    fun restoreMultipleDialog() = restoreDialogEventManager.setMultiple()

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

    sealed class FetchResult {
        data class Success(val schedule: ScheduleEntity) : FetchResult()
        data class Error(val message: String, val error: Throwable) : FetchResult()
        data class Loading(val isLoading: Boolean) : FetchResult()
    }
}