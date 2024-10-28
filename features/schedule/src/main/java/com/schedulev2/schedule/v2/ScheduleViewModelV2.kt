package com.schedulev2.schedule.v2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.schedulev2.data.repositories.di.SingleImplementation
import com.schedulev2.data.repositories.v2.schedule.repository.impl.AppConfigRepositoryV2
import com.schedulev2.data.repositories.v2.schedule.repository.ScheduleRepository
import com.schedulev2.data.event_manager.RefreshEventManager
import com.schedulev2.schedule.v1.ScheduleFragmentContract
import com.schedulev2.schedule.v2.adapter.viewpager.model.ViewPagerItem
import com.schedulev2.utils.Result
import com.schedulev2.utils.sources.SingleEvent
import com.schedulev2.utils.sources.SingleLiveData
import com.schedulev2.utils.sources.SingleMutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModelV2 @Inject constructor(
    private val router: ScheduleFragmentContract,
    @SingleImplementation private val scheduleRepository: ScheduleRepository,
    private val appConfigRepositoryV2: AppConfigRepositoryV2,
    private val refreshEventManager: RefreshEventManager

) : ViewModel() {

    private val disposables = CompositeDisposable()

    /** tabs bar initialization */
    private val _testLiveData = MutableLiveData<Result<List<ViewPagerItem>>>()
    val testLiveData: LiveData<Result<List<ViewPagerItem>>> = _testLiveData

    /** recycler view initialization */
    private val _weeksLiveData = MutableLiveData<ResultWeeksConfig<LastWeeksConfig>>()
    val weeksLiveData: LiveData<ResultWeeksConfig<LastWeeksConfig>> = _weeksLiveData

    /** start initialization each time when call */
    val initScheduleLiveData: LiveData<SingleEvent<Boolean>> = refreshEventManager.getRefreshLiveData()

    /** need to block tabs click */
    private val _blockingTabsClicks = SingleMutableLiveData<Boolean>()
    val blockingTabsClicks: SingleLiveData<Boolean> = _blockingTabsClicks

    private var lastScheduleList: List<ViewPagerItem>? = null
    private var lastWeekConfig: LastWeeksConfig? = null

    var isBackStackReturn = true

    fun restoreStateAfterPopBackStack() {
//        if (lastScheduleList != null && lastWeekConfig != null) {
//            _blockingTabsClicks.value = false
//            _weeksLiveData.value =
//                (ResultWeeksConfig.Success(lastWeekConfig!!.copy(isRepeating = false)))
//            _testLiveData.value = (Result.Success(lastScheduleList!!))
//        }
    }

    init {
        initializeSchedule()
    }

    fun initSchedule() {
        _testLiveData.value = Result.Error(Throwable("Обновление данных"))
        _weeksLiveData.value = ResultWeeksConfig.Error(Throwable("Обновление данных"))
        lastScheduleList = null
        lastWeekConfig = null
        Log.d("ScheduleViewModelV2", "init")
        _weeksLiveData.value = ResultWeeksConfig.InitLoading
        _testLiveData.value = Result.Loading
        setBlockingTabsClicks(false)
        scheduleRepository.doFetch().observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    Log.d("ScheduleViewModelV2", "init onSuccess")
                    lastWeekConfig = LastWeeksConfig(
                        it.weeks,
                        it.currentWeek,
                        lastWeekConfig != null
                    )
//                    lastScheduleList = mapList(it.scheduleList)
                    _weeksLiveData.value = (ResultWeeksConfig.Success(lastWeekConfig!!))
                    _testLiveData.value = (Result.Success(lastScheduleList!!))
                },
                onError = {
                    when (it) {
                        is UnknownHostException -> {
                            Log.d("ScheduleViewModelV2", "init onError:${it} ${it.message}")
                            _weeksLiveData.value = (ResultWeeksConfig.Error(Throwable("Отсутствует подключение к интернету")))
                            _testLiveData.value = (Result.Error(Throwable("Отсутствует подключение к интернету")))
                        }
                        else -> {
                            Log.d("ScheduleViewModelV2", "init onError:${it} ${it.message}")
                            _weeksLiveData.value = (ResultWeeksConfig.Error(it))
                            _testLiveData.value = (Result.Error(it))
                        }
                    }

                }
            ).addTo(disposables)
    }

    fun initializeSchedule() {
        refreshEventManager.setRefreshLiveData()
    }

    fun setBlockingTabsClicks(value: Boolean) {
        _blockingTabsClicks.value = SingleEvent(value)
    }

    fun fetchByWeek(week: String) {
        _weeksLiveData.value = ResultWeeksConfig.Loading
        _testLiveData.value = Result.Loading
        scheduleRepository.doFetchWeek(week).observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    Log.d("ScheduleViewModelV2", "fetchByWeek onSuccess")
                    lastWeekConfig = LastWeeksConfig(it.weeks, it.currentWeek, true)
//                    lastScheduleList = mapList(it.scheduleList)
                    _weeksLiveData.value = (ResultWeeksConfig.Success(lastWeekConfig!!))
                    _testLiveData.value = (Result.Success(lastScheduleList!!))

                },
                onError = {
                    when (it) {
                        is UnknownHostException -> {
                            Log.d("ScheduleViewModelV2", "fetchByWeek onError:${it} ${it.message}")
                            _weeksLiveData.value = (ResultWeeksConfig.ErrorFetchWeek(
                                Throwable("Отсутствует подключение к интернету"),
                                lastWeekConfig!!.copy(isRepeating = true)
                            ))
                            _testLiveData.value = (Result.Error(Throwable("Отсутствует подключение к интернету")))
                        }
                        else -> {
                            Log.d("ScheduleViewModelV2", "fetchByWeek onError:${it} ${it.message}")
                            _weeksLiveData.value = (ResultWeeksConfig.ErrorFetchWeek(
                                it,
                                lastWeekConfig!!.copy(isRepeating = true)
                            ))
                            _testLiveData.value = (Result.Error(it))
                        }
                    }
                }
            ).addTo(disposables)
    }


    fun restoreAdapter() {
        val value = scheduleRepository.restore() ?: return
//        _testLiveData.value = Result.Success(mapList(value))
    }

    fun navigateToSettingsScreen() = router.navigateToSettingsScreen()

//    private fun mapList(list: List<ViewPagerItemDomain>): List<ViewPagerItem> {
//        return list.map { item ->
//            when (item) {
//                is ViewPagerItemDomain.RecyclerViewDay -> ViewPagerItem.RecyclerViewDay(
//                    mapItems(
//                        item.lessons
//                    )
//                )
//
//                is ViewPagerItemDomain.RecyclerViewCurrentDay -> ViewPagerItem.RecyclerViewCurrentDay(
//                    mapItems(item.lessons)
//                )
//            }
//        }
//    }
//
//    private fun mapItems(list: List<TimetableItemDomain>): List<TimetableItem> {
//        return list.map { item ->
//            when (item) {
//                is TimetableItemDomain.Title -> TimetableItem.Title(
//                    item.date,
//                    item.dayOfWeekName,
//                    item.groupName,
//                    item.isTitleEnabled
//                )
//
//                is TimetableItemDomain.Lesson -> TimetableItem.Lesson(item.time, item.lessonName, mapItems(item.lessonContentTypeDomain))
//                is TimetableItemDomain.Break -> TimetableItem.Break(
//                    item.time,
//                    item.lessonName,
//                    item.progressValue
//                )
//
//                is TimetableItemDomain.LessonCurrent -> TimetableItem.LessonCurrent(
//                    item.time,
//                    item.lessonName,
//                    item.progressValue
//                )
//
//                is TimetableItemDomain.TitleCurrent -> TimetableItem.TitleCurrent(
//                    item.date,
//                    item.dayOfWeekName,
//                    item.groupName,
//                    item.isTitleEnabled
//                )
//            }
//        }
//
//    }

    fun getTitle() = appConfigRepositoryV2.getTitle()

    private fun setWeekConfig(config: ScheduleRepository.ScheduleConfig): LastWeeksConfig {
        lastWeekConfig?.let { result ->
            return LastWeeksConfig(
                result.weeks,
                result.currentWeek,
                true
            )
        }
        return LastWeeksConfig(config.weeks, config.currentWeek, false)
    }


    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    data class LastWeeksConfig(
        val weeks: List<Int>,
        val currentWeek: Int,
        val isRepeating: Boolean
    )


    sealed interface ResultWeeksConfig<out T> {
        data class Success<T>(val data: T) : ResultWeeksConfig<T>
        data class Error(val exception: Throwable) : ResultWeeksConfig<Nothing>
        data class ErrorFetchWeek(val exception: Throwable, val oldValue: LastWeeksConfig) :
            ResultWeeksConfig<Nothing>

        data object Loading : ResultWeeksConfig<Nothing>
        data object InitLoading : ResultWeeksConfig<Nothing>
    }

}