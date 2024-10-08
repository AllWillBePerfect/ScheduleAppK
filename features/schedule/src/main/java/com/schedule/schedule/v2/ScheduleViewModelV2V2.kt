package com.schedule.schedule.v2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.schedule.data.repositories.di.ContainerImplementation
import com.schedule.data.repositories.v2.schedule.repository.impl.AppConfigRepositoryV2
import com.schedule.data.repositories.v2.schedule.repository.ScheduleRepository
import com.schedule.data.event_manager.RefreshEventManager
import com.schedule.data.event_manager.RestoreAfterPopBackStackEventManager
import com.schedule.data.repositories.settings.AdditionalOptionRepository
import com.schedule.models.sharpref.v2.AppStateV2
import com.schedule.models.ui.v2.schedule.TimetableItemDomain
import com.schedule.models.ui.v2.schedule.ViewPagerItemDomain
import com.schedule.schedule.v1.ScheduleFragmentContract
import com.schedule.schedule.v2.adapter.recyclerview.model.TimetableItem
import com.schedule.schedule.v2.adapter.viewpager.model.ViewPagerItem
import com.schedule.utils.Result
import com.schedule.utils.sources.SingleEvent
import com.schedule.utils.sources.SingleLiveData
import com.schedule.utils.sources.SingleMutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModelV2V2 @Inject constructor(
    private val router: ScheduleFragmentContract,
    @ContainerImplementation private val scheduleRepository: ScheduleRepository,
    private val appConfigRepositoryV2: AppConfigRepositoryV2,
    private val additionalOptionRepository: AdditionalOptionRepository,
    private val refreshEventManager: RefreshEventManager,
    private val restoreAfterPopBackStackEventManager: RestoreAfterPopBackStackEventManager
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _initLiveData = SingleMutableLiveData<Result<ScheduleInitConfig>>()
    val initLiveData: SingleLiveData<Result<ScheduleInitConfig>> = _initLiveData

    private val _weekFetchLiveData = SingleMutableLiveData<Result<ScheduleInitConfig>>()
    val weekFetchLiveData: SingleLiveData<Result<ScheduleInitConfig>> = _weekFetchLiveData

    val refreshServiceLiveData: LiveData<SingleEvent<Boolean>> =
        refreshEventManager.getRefreshLiveData()

    private val _backStackRestoreLiveData = SingleMutableLiveData<ScheduleInitConfig>()
    val backStackRestoreLiveData: SingleLiveData<ScheduleInitConfig> = _backStackRestoreLiveData

    private var scheduleInitConfig: ScheduleInitConfig? = null


    private val _appBarLoadingLiveData = SingleMutableLiveData<AppBarLoadingLiveDataState>()
    val appBarLoadingLiveData: SingleLiveData<AppBarLoadingLiveDataState> = _appBarLoadingLiveData

    fun setAppBarLoadingLiveDataLoadedState() {
        _appBarLoadingLiveData.value = SingleEvent(AppBarLoadingLiveDataState.Loaded)
    }

    fun setAppBarLoadingLiveDataLoadingState() {
        _appBarLoadingLiveData.value = SingleEvent(AppBarLoadingLiveDataState.Loading)
    }

    val restoreAfterPopBackStackServiceLiveData: SingleLiveData<Boolean> =
        restoreAfterPopBackStackEventManager.getRestoreStateAfterPopBackStackLiveData()

    private val _tabsLayoutLiveData =
        SingleMutableLiveData<TabsLayoutLiveDataState<ScheduleInitConfig>>()
    val tabsLayoutLiveData: SingleLiveData<TabsLayoutLiveDataState<ScheduleInitConfig>> =
        _tabsLayoutLiveData

    private val _adapterLiveData = SingleMutableLiveData<AdapterLiveDataState<ScheduleInitConfig>>()
    val adapterLiveData: SingleLiveData<AdapterLiveDataState<ScheduleInitConfig>> = _adapterLiveData

    private var scheduleInitConfigV2: ScheduleInitConfig? = null
    var isBackStackReturn = true


    fun initV2() {
        Log.d("ScheduleViewModelV2V2", "init")
        setAppBarLoadingLiveDataLoadingState()
        _tabsLayoutLiveData.value = SingleEvent(TabsLayoutLiveDataState.LoadingShimmer)
        _adapterLiveData.value = SingleEvent(AdapterLiveDataState.Loading)
        scheduleRepository.doFetch().observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    Log.d("ScheduleViewModelV2V2", "init onSuccess")
                    val scheduleConfig = ScheduleInitConfig(
                        scheduleList = mapList(it.scheduleList),
                        currentWeek = it.currentWeek,
                        weeks = it.weeks
                    )
                    scheduleInitConfigV2 = scheduleConfig
                    _adapterLiveData.value = SingleEvent(
                        AdapterLiveDataState.Success(scheduleConfig)
                    )
                    _tabsLayoutLiveData.value = SingleEvent(
                        TabsLayoutLiveDataState.SuccessInit(scheduleConfig)
                    )

                },
                onError = {
                    Log.d("ScheduleViewModelV2V2", "init onError:${it} ${it.message}")
                    _adapterLiveData.value = SingleEvent(
                        AdapterLiveDataState.Error(it)
                    )
                    _tabsLayoutLiveData.value = SingleEvent(
                        TabsLayoutLiveDataState.Error(it)
                    )
                }
            )
            .addTo(disposables)
    }

    fun fetchByWeekV2(week: String) {
        Log.d("ScheduleViewModelV2V2", "fetchByWeek: $week")
        setAppBarLoadingLiveDataLoadingState()
        _tabsLayoutLiveData.value = SingleEvent(TabsLayoutLiveDataState.Loading)
        _adapterLiveData.value = SingleEvent(AdapterLiveDataState.Loading)
        scheduleRepository.doFetchWeek(week).observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    Log.d("ScheduleViewModelV2V2", "fetchByWeek onSuccess")
                    val scheduleConfig = ScheduleInitConfig(
                        scheduleList = mapList(it.scheduleList),
                        currentWeek = it.currentWeek,
                        weeks = it.weeks
                    )
                    scheduleInitConfigV2 = scheduleConfig
                    _adapterLiveData.value = SingleEvent(
                        AdapterLiveDataState.Success(scheduleConfig)
                    )
                    _tabsLayoutLiveData.value = SingleEvent(
                        TabsLayoutLiveDataState.SuccessWeekFetch(scheduleConfig)
                    )
                },
                onError = {
                    Log.d("ScheduleViewModelV2V2", "fetchByWeek onError:${it} ${it.message}")
                    _tabsLayoutLiveData.value =
                        SingleEvent(TabsLayoutLiveDataState.NotLoadedWeekError(scheduleInitConfigV2!!.currentWeek))
                }
            )
            .addTo(disposables)
    }

    fun onResume() {
//        val value = scheduleRepository.restoreEntity() ?: return
//        _adapterLiveData.value = SingleEvent(AdapterLiveDataState.Success(ScheduleInitConfig(
//            scheduleList = mapList(value.scheduleList),
//            currentWeek = value.currentWeek,
//            weeks = value.weeks
//        )))
        val scheduleEntity = scheduleRepository.restoreEntity() ?: return
//        scheduleInitConfigV2?.let {
        _adapterLiveData.value = SingleEvent(
            AdapterLiveDataState.Success(
                ScheduleInitConfig(
                    scheduleList = mapList(scheduleEntity.scheduleList),
                    currentWeek = scheduleEntity.currentWeek,
                    weeks = scheduleEntity.weeks
                )
            )
        )
//        }

    }

    fun restoreStateAfterPopBackStack() {
        scheduleInitConfigV2?.let {
            val config: ScheduleInitConfig = if (scheduleRepository.restoreEntity() == null)
                it
            else {
                val entity = scheduleRepository.restoreEntity()!!
                ScheduleInitConfig(
                    scheduleList = mapList(entity.scheduleList),
                    weeks = entity.weeks,
                    currentWeek = entity.currentWeek
                )
            }
            _adapterLiveData.value = SingleEvent(AdapterLiveDataState.Success(config))
            _tabsLayoutLiveData.value = SingleEvent(TabsLayoutLiveDataState.SuccessInit(config))
        }
    }

    fun restoreStatePopBackStack() {
        if (refreshEventManager.getRefreshLiveData().value?.eventForCheck == null) {
            restoreAfterPopBackStackEventManager.setRestoreStateAfterPopBackStackLiveData()
        }
    }

    fun manageViewMultipleScrollTabLayout(show: () -> Unit, hide: () -> Unit) {
        val state = appConfigRepositoryV2.getAppState()
        when (state) {
            is AppStateV2.Single -> hide.invoke()
            is AppStateV2.Multiple -> if (additionalOptionRepository.getMultipleGroupFastScrollState()) show.invoke() else hide.invoke()
            is AppStateV2.Replace -> hide.invoke()
            is AppStateV2.Unselected -> hide.invoke()
        }
    }


    init {
//        init()
        initV2()
    }

    fun init() {
        Log.d("ScheduleViewModelV2V2", "init")
        scheduleInitConfig = null
        _initLiveData.value = SingleEvent(Result.Loading)
        scheduleRepository.doFetch().observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    Log.d("ScheduleViewModelV2V2", "init onSuccess")
                    scheduleInitConfig = ScheduleInitConfig(
                        scheduleList = mapList(it.scheduleList),
                        currentWeek = it.currentWeek,
                        weeks = it.weeks
                    )
                    _initLiveData.value = SingleEvent(
                        Result.Success(
                            scheduleInitConfig!!
                        )
                    )

                },
                onError = {
                    Log.d("ScheduleViewModelV2V2", "init onError:${it} ${it.message}")
                    _initLiveData.value = SingleEvent(Result.Error(it))
                }
            )
            .addTo(disposables)
    }

    fun fetchByWeek(week: String) {
        Log.d("ScheduleViewModelV2V2", "fetchByWeek: $week")
        _weekFetchLiveData.value = SingleEvent(Result.Loading)
        scheduleRepository.doFetchWeek(week).observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    Log.d("ScheduleViewModelV2V2", "fetchByWeek onSuccess")
                    _weekFetchLiveData.value = SingleEvent(
                        Result.Success(
                            ScheduleInitConfig(
                                scheduleList = mapList(it.scheduleList),
                                currentWeek = it.currentWeek,
                                weeks = it.weeks
                            )
                        )
                    )
                },
                onError = {
                    Log.d("ScheduleViewModelV2V2", "fetchByWeek onError:${it} ${it.message}")
                    _weekFetchLiveData.value = SingleEvent(Result.Error(it))
                }
            )
            .addTo(disposables)
    }

    fun getTitle() = appConfigRepositoryV2.getTitle()

    private fun mapList(list: List<ViewPagerItemDomain>): List<ViewPagerItem> {
        return list.map { item ->
            when (item) {
                is ViewPagerItemDomain.RecyclerViewDay -> ViewPagerItem.RecyclerViewDay(
                    mapItems(
                        item.lessons
                    )
                )

                is ViewPagerItemDomain.RecyclerViewCurrentDay -> ViewPagerItem.RecyclerViewCurrentDay(
                    mapItems(item.lessons)
                )
            }
        }
    }

    private fun mapItems(list: List<TimetableItemDomain>): List<TimetableItem> {
        return list.map { item ->
            when (item) {
                is TimetableItemDomain.Title -> TimetableItem.Title(
                    item.date,
                    item.dayOfWeekName,
                    item.groupName,
                    item.isTitleEnabled
                )

                is TimetableItemDomain.Lesson -> TimetableItem.Lesson(
                    item.time,
                    item.lessonName,
                    mapContentType(item.lessonContentTypeDomain)
                )

                is TimetableItemDomain.Break -> TimetableItem.Break(
                    item.time,
                    item.lessonName,
                    item.progressValue
                )

                is TimetableItemDomain.LessonCurrent -> TimetableItem.LessonCurrent(
                    item.time,
                    item.lessonName,
                    item.progressValue,
                    mapContentType(item.lessonContentTypeDomain)
                )

                is TimetableItemDomain.TitleCurrent -> TimetableItem.TitleCurrent(
                    item.date,
                    item.dayOfWeekName,
                    item.groupName,
                    item.isTitleEnabled
                )
            }
        }

    }

    private fun mapContentType(domain: TimetableItemDomain.Companion.ContentTypeDomain): TimetableItem.ContentType {
        return when (domain) {
            TimetableItemDomain.Companion.ContentTypeDomain.NONE -> TimetableItem.ContentType.NONE
            TimetableItemDomain.Companion.ContentTypeDomain.ONLINE -> TimetableItem.ContentType.ONLINE
            TimetableItemDomain.Companion.ContentTypeDomain.OFFLINE -> TimetableItem.ContentType.OFFLINE
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }


    data class ScheduleInitConfig(
        val scheduleList: List<ViewPagerItem>,
        val currentWeek: Int,
        val weeks: List<Int>
    )

    sealed interface AppBarLoadingLiveDataState {
        data object Loaded : AppBarLoadingLiveDataState
        data object Loading : AppBarLoadingLiveDataState
    }

    sealed interface TabsLayoutLiveDataState<out T> {
        data class SuccessInit<T>(val data: T) : TabsLayoutLiveDataState<T>
        data class SuccessWeekFetch<T>(val data: T) : TabsLayoutLiveDataState<T>
        data class Error(val exception: Throwable) : TabsLayoutLiveDataState<Nothing>
        data class NotLoadedWeekError(val oldValue: Int) : TabsLayoutLiveDataState<Nothing>
        data object Loading : TabsLayoutLiveDataState<Nothing>
        data object LoadingShimmer : TabsLayoutLiveDataState<Nothing>
    }

    sealed interface AdapterLiveDataState<out T> {
        data class Success<T>(val data: T) : AdapterLiveDataState<T>
        data class Error(val exception: Throwable) : AdapterLiveDataState<Nothing>
        data object Loading : AdapterLiveDataState<Nothing>
        data object LoadingShimmer : AdapterLiveDataState<Nothing>
    }

}