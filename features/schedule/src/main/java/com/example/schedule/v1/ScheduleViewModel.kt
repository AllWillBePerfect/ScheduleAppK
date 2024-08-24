package com.example.schedule.v1

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.data.repositories.AppConfigRepository
import com.example.data.repositories.ScheduleApiRepository
import com.example.data.repositories.ScheduleItemListRepositoryV3
import com.example.data.service.RefreshService
import com.example.models.ui.ScheduleGroupsListEntity
import com.example.utils.Result
import com.example.utils.ScheduleItem
import com.example.utils.ScheduleResult
import com.example.utils.sources.SingleEvent
import com.example.utils.sources.SingleLiveData
import com.example.utils.sources.SingleMutableLiveData
import com.example.views.adapter.GroupItem
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.Single
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
class ScheduleViewModel @Inject constructor(
    private val appConfigRepository: AppConfigRepository,
    private val scheduleItemListRepositoryV3: ScheduleItemListRepositoryV3,
    private val scheduleApiRepository: ScheduleApiRepository,
    private val refreshService: RefreshService

) : ViewModel() {

    private val disposables = CompositeDisposable()
    private var isFirstLaunch: Boolean = true


    private val _scheduleLiveData = SingleMutableLiveData<List<ScheduleItem>>()
    val scheduleLiveData: SingleLiveData<List<ScheduleItem>> = _scheduleLiveData

    private val _weekViewModel = SingleMutableLiveData<WeekConfigContainer>()
    val weekViewModel: SingleLiveData<WeekConfigContainer> = _weekViewModel

    private val _loadingAppBarLiveData = SingleMutableLiveData<AppBarLoading>()
    val loadingAppBarLiveData: SingleLiveData<AppBarLoading> = _loadingAppBarLiveData


    private val editTextSubject = PublishSubject.create<CharSequence>()
    private fun getEditTextSubject(): Observable<CharSequence> = editTextSubject.hide().share()
    fun editTextSet(charSequence: CharSequence) {
        editTextSubject.onNext(charSequence)
    }

    private val groupsSubject =
        BehaviorSubject.create<List<ScheduleGroupsListEntity.ScheduleGroupEntity>>()

    private fun getGroupsSubject(): Observable<List<ScheduleGroupsListEntity.ScheduleGroupEntity>> =
        groupsSubject.hide().share()

    private val _groupsLiveData = SingleMutableLiveData<List<GroupItem>>(SingleEvent(emptyList()))
    val groupsLiveData: SingleLiveData<List<GroupItem>> = _groupsLiveData

    private val _groupsErrorsLiveData = SingleMutableLiveData<GroupError>()
    val groupsErrorsLiveData: SingleLiveData<GroupError> = _groupsErrorsLiveData

    private val _closeGroupSearchView = MutableLiveData<Boolean>()
    val closeGroupSearchView: LiveData<Boolean> = _closeGroupSearchView

    private var _restoreWeeksConfig: ScheduleItemListRepositoryV3.WeeksConfig? = null


    init {
        init(appConfigRepository.getSingleGroup().groupName)
        setupGroupObserver()
    }

    fun restoreWeeksTabsState() {
        if (_restoreWeeksConfig != null)
        _weekViewModel.value = SingleEvent(WeekConfigContainer.FromNetwork(_restoreWeeksConfig!!))
    }

    private fun setupGroupObserver() {
        getEditTextSubject()
            .map {
//                _loadingAppBarLiveData.postValue(AppBarLoading.Loading)
                it
            }
            .filter {
                val value = it.isNotEmpty()
//                if (!value) _loadingAppBarLiveData.postValue(AppBarLoading.Loaded)
                value
            }
            .debounceIf({ it.length > 1 }, 450L, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io()).switchMap {
                scheduleApiRepository.fetchGroupListObservable(it.toString())
                    .map { it.copy(choices = it.choices.sortedBy { group -> group.name }) }
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
//                _loadingAppBarLiveData.postValue(false)
                if (it.choices.isNullOrEmpty()) {
                    groupsSubject.onNext(groupsSubject.value ?: emptyList())
                } else {
                    groupsSubject.onNext(it.choices)
                }
            }, onError = {
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
                _groupsLiveData.value = SingleEvent(list.map { GroupItem(it.name, it.group) })
            }.addTo(disposables)
        Log.d("EnterViewModel Config", appConfigRepository.getAppState().name)
    }

    fun getTitle() = appConfigRepository.getSingleGroup().groupName
    fun getIsFirstLaunch(): Boolean = isFirstLaunch
    fun changeIsFirstLaunch(state: Boolean) {
        isFirstLaunch = state
    }

    fun initGroup(groupName: String) {

        Single.just(groupName).toObservable()
            .switchMap {
                scheduleApiRepository.fetchScheduleByGroupNameSingle(groupName).toObservable()
                    .onErrorResumeNext(Single
                        .just(groupName).toObservable()
                        .flatMap {
                            scheduleApiRepository.fetchGroupListSingle(it).toObservable()
                        }
                        .map { it.copy(choices = it.choices.sortedBy { group -> group.name }) }
                        .flatMap {
                            scheduleApiRepository.fetchScheduleByHtmlSingle((it.choices.find { it.name == groupName }
                                ?: it.choices.first()).group).toObservable()
                        })
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    Log.d("EnterViewModel fetch", it.toString())
//                    _fetchScheduleLiveData.value = FetchResult.Loading(false)
                    appConfigRepository.setSingleGroup(it.name)
                    init(it.name)
                    _closeGroupSearchView.value = true
//                    _fetchScheduleLiveData.value = FetchResult.Success(it)
                },
                onError = {
                    if (it is UnknownHostException)
                        Log.d("EnterViewModel error", "No network connection")
                    else
                        Log.d("EnterViewModel error", it.toString())
//                    _fetchScheduleLiveData.value = FetchResult.Loading(false)
//                    _fetchScheduleLiveData.value = FetchResult.Error(it.message.toString(), it)
                    _groupsErrorsLiveData.value = SingleEvent(GroupError(it.message.toString(), it))
                },
                onComplete = {
                    Log.d("EnterViewModel complete", "fetch Completed")
//                    _fetchScheduleLiveData.value = FetchResult.Loading(false)
                }
            )
            .addTo(disposables)
    }

    fun init(
        groupName: String = appConfigRepository.getSingleGroup().groupName,
        fromDB: Observable<ScheduleResult<ScheduleItemListRepositoryV3.ScheduleWeeksConfigContainer>>? = null
    ) {
        val fromDb = scheduleItemListRepositoryV3.getWeeksConfigFromDB(groupName)
        val fromNetwork = scheduleItemListRepositoryV3.fetchWeeksConfig(groupName)
//        Observable.concat(fromDb, fromNetwork)

        (fromDB ?: fromNetwork)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { res ->
                    when (res) {
                        is ScheduleResult.SuccessFromDB -> {
                            Log.d(
                                "ScheduleViewModel compareWeek fromDB success",
                                res.data.toString()
                            )
                            _loadingAppBarLiveData.value =
                                SingleEvent(AppBarLoading.Loaded(res.data.scheduleStatus))
                            _weekViewModel.value =
                                SingleEvent(WeekConfigContainer.FromDB(res.data.weeksConfig))
                            updateList()
                            _restoreWeeksConfig = res.data.weeksConfig
                        }

                        is ScheduleResult.SuccessFromNetwork -> {
                            Log.d(
                                "ScheduleViewModel compareWeek fromNetwork success",
                                res.data.toString()
                            )
                            _loadingAppBarLiveData.value =
                                SingleEvent(AppBarLoading.Loaded(res.data.scheduleStatus))
                            _weekViewModel.value =
                                SingleEvent(WeekConfigContainer.FromNetwork(res.data.weeksConfig))
                            updateList()
                            _restoreWeeksConfig = res.data.weeksConfig

                        }

                        is ScheduleResult.Error -> {
                            Log.d(
                                "ScheduleViewModel compareWeek error",
                                res.exception.message.toString()
                            )
                            _loadingAppBarLiveData.value = SingleEvent(AppBarLoading.LoadedError)
                            if (fromDB == null)
                                init(groupName, fromDb)
                        }

                        is ScheduleResult.Loading -> {
                            _loadingAppBarLiveData.value = SingleEvent(AppBarLoading.Loading)
                            Log.d("ScheduleViewModel compareWeek loading", "Loading")
                        }
                    }
                },
                onError = {
                    Log.d(
                        "ScheduleViewModelV2 weeksConfig onError",
                        it.message.toString()
                    )
                },
                onComplete = {
                    Log.d(
                        "ScheduleViewModelV2 weeksConfig onComplete",
                        "onComplete weeksConfig"
                    )
                }
            ).addTo(disposables)
    }

    fun fetchWeekSchedule(
        groupName: String = appConfigRepository.getSingleGroup().groupName,
        week: Int? = null,
        fromDB: Observable<ScheduleResult<ScheduleItemListRepositoryV3.ScheduleListContainer>>? = null
    ) {
        _loadingAppBarLiveData.value = SingleEvent(AppBarLoading.Loading)
        val fromDb = scheduleItemListRepositoryV3.getScheduleFromDb(groupName, week)
        val fromNetwork = scheduleItemListRepositoryV3.fetchSchedule(groupName, week)
//        Observable.concat(fromDb, fromNetwork)
        (fromDB ?: fromNetwork)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { res ->
                    when (res) {
                        is ScheduleResult.SuccessFromDB -> {

                            Log.d("ScheduleViewModel schedule fromDB success", res.data.toString())
                            _loadingAppBarLiveData.value =
                                SingleEvent(AppBarLoading.Loaded(res.data.scheduleStatus))
                            _scheduleLiveData.value = SingleEvent(res.data.list)
                        }

                        is ScheduleResult.SuccessFromNetwork -> {

                            Log.d(
                                "ScheduleViewModel schedule fromNetwork success",
                                res.data.toString()
                            )
                            _loadingAppBarLiveData.value =
                                SingleEvent(AppBarLoading.Loaded(res.data.scheduleStatus))
                            _scheduleLiveData.value = SingleEvent(res.data.list)
                        }

                        is ScheduleResult.Error -> {
                            Log.d(
                                "ScheduleViewModel schedule error",
                                res.exception.message.toString()
                            )
                            _loadingAppBarLiveData.value = SingleEvent(AppBarLoading.LoadedError)
                            if (fromDB == null)
                                fetchWeekSchedule(groupName, week, fromDb)
                        }

                        is ScheduleResult.Loading -> {
                            _loadingAppBarLiveData.value = SingleEvent(AppBarLoading.Loading)
                            Log.d("ScheduleViewModel schedule loading", "Loading")
                        }
                    }
                },
                onError = { Log.d("ScheduleViewModelV2 schedule onError", it.message.toString()) },
                onComplete = {
                    Log.d(
                        "ScheduleViewModelV2 schedule onComplete",
                        "onComplete schedule"
                    )
                }
            ).addTo(disposables)
    }

    fun updateList() {
        scheduleItemListRepositoryV3.reuseList()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnDispose { Log.d("ScheduleViewModel reuse dispose", "dispose") }
            .subscribeBy(
                onNext = { res ->
                    when (res) {
                        is Result.Success -> {
                            Log.d("ScheduleViewModel reuse success", res.data.toString())
                            _scheduleLiveData.value = SingleEvent(res.data.list)
                        }

                        is Result.Error -> Log.d(
                            "ScheduleViewModel reuse error",
                            res.exception.message.toString()
                        )

                        is Result.Loading -> Log.d(
                            "ScheduleViewModel reuse loading",
                            "Loading"
                        )
                    }
                },
                onError = { Log.d("ScheduleViewModelV2 reuse onError", it.message.toString()) },
                onComplete = { Log.d("ScheduleViewModelV2 reuse onComplete", "onComplete reuse ") }
            ).addTo(disposables)
    }

    fun getRefreshLiveData() = refreshService.getRefreshLiveData()


    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

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

    sealed interface AppBarLoading {
        data object Loading : AppBarLoading
        data class Loaded(val scheduleStatus: ScheduleItemListRepositoryV3.ScheduleListContainer.ScheduleStatus) :
            AppBarLoading

        data object LoadedError : AppBarLoading
    }

    sealed class WeekConfigContainer(open val weeksConfig: ScheduleItemListRepositoryV3.WeeksConfig) {
        data class FromDB(override val weeksConfig: ScheduleItemListRepositoryV3.WeeksConfig) :
            WeekConfigContainer(weeksConfig)

        data class FromNetwork(override val weeksConfig: ScheduleItemListRepositoryV3.WeeksConfig) :
            WeekConfigContainer(weeksConfig)
    }

    data class GroupError(val message: String, val error: Throwable)
}