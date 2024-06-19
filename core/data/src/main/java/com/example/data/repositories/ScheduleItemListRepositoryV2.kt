package com.example.data.repositories

import android.util.Log
import com.example.data.mappers.toDbo
import com.example.data.mappers.toEntity
import com.example.database.dao.ScheduleDao
import com.example.models.ui.ScheduleEntity
import com.example.utils.Result
import com.example.utils.ScheduleItem
import com.example.utils.sources.CurrentDayOfWeekUtils
import com.example.utils.sources.DateUtils
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton
@Deprecated("Deprecated")
interface ScheduleItemListRepositoryV2 {


    fun fetchWeekConfigObservable(groupName: String): Observable<Result<ScheduleItemListRepository.WeeksConfig>>
    fun fetchScheduleV2Observable(scheduleFetchV2: ScheduleFetchV2): Observable<Result<List<ScheduleItem>>>
    fun fetchScheduleObservable(scheduleFetch: ScheduleFetch): Observable<Result<List<ScheduleItem>>>
    fun reuseListResultObservable(): Observable<Result<List<ScheduleItem>>>

    fun getWeekConfigFromDB(groupName: String): Observable<Result<ScheduleItemListRepository.WeeksConfig>>
    fun getScheduleFromDb(groupName: String, week: Int? = null): Observable<Result<List<ScheduleItem>>>
    fun reuseListResultFromDb()

    @Deprecated("Deprecated")
    @Singleton
    class Impl @Inject constructor(
        private val currentLessonRepository: CurrentLessonRepository,
        private val scheduleApiRepository: ScheduleApiRepository,
        private val appConfigRepository: AppConfigRepository,
        private val scheduleDao: ScheduleDao
    ) : ScheduleItemListRepositoryV2 {

        var compareDayOfWeek: Int? = null
        var compareDate: String? = null
        var compareWeek: Int? = null
        var compareScheduleEntity: ScheduleEntity? = null


        override fun fetchWeekConfigObservable(groupName: String): Observable<Result<ScheduleItemListRepository.WeeksConfig>> {
            return Observable.create {
                it.onNext(groupName)
                it.onComplete()
            }.flatMap { groupName ->
                scheduleApiRepository.fetchScheduleByGroupNameObservable(groupName)
                    .onErrorResumeNext(
                        Observable.just(groupName)
                            .flatMap { scheduleApiRepository.fetchGroupListObservable(groupName) }
                            .flatMap { scheduleApiRepository.fetchScheduleByHtmlObservable(it.choices.first().group) }
                    ).map {
                        compareWeek = it.week
                        compareScheduleEntity = it

                        Completable.fromRunnable {
                            scheduleDao.insertOrUpdateCompareTable(it.toDbo(true))
                        }.subscribeOn(Schedulers.io()).subscribe()

                        Result.Success(
                            ScheduleItemListRepository.WeeksConfig(
                                it.week,
                                it.weeks
                            )
                        ) as Result<ScheduleItemListRepository.WeeksConfig>

                    }
                    .startWith(Result.Loading)
                    .onErrorReturn { error -> Result.Error(error) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())

            }
        }

        override fun fetchScheduleObservable(scheduleFetch: ScheduleFetch): Observable<Result<List<ScheduleItem>>> {
            return Observable.create {
                it.onNext(scheduleFetch)
                it.onComplete()
            }.switchMap { schedule ->
                when (schedule) {
                    is ScheduleFetch.Init -> {
                        scheduleApiRepository.fetchScheduleByGroupNameObservable(schedule.groupName)
                            .onErrorResumeNext(Observable.just(schedule)
                                .flatMap {
                                    scheduleApiRepository.fetchGroupListObservable(schedule.groupName)
                                }
                                .flatMap {
                                    scheduleApiRepository.fetchScheduleByHtmlObservable(
                                        it.choices.first().group
                                    )
                                })
                    }

                    is ScheduleFetch.ByWeek -> {
                        scheduleApiRepository.fetchScheduleByWeekObservable(
                            compareScheduleEntity!!.group,
                            schedule.week.toString()
                        )
                            .onErrorResumeNext(Observable.just(schedule)
                                .flatMap {

                                    scheduleApiRepository.fetchGroupListObservable(schedule.groupName)
                                }
                                .flatMap {

                                    scheduleApiRepository.fetchScheduleByWeekObservable(
                                        it.choices.first().group,
                                        schedule.week.toString()
                                    )
                                })
                    }
                }
            }.map {
                when (scheduleFetch) {
                    is ScheduleFetch.Init -> {
                        compareDayOfWeek = CurrentDayOfWeekUtils.getCurrentDayOfWeek()
                        compareDate = DateUtils.getCurrentDate()
                        compareWeek = it.week
                        compareScheduleEntity = it
                    }

                    is ScheduleFetch.ByWeek -> {
                        compareDayOfWeek = CurrentDayOfWeekUtils.getCurrentDayOfWeek()
                        compareDate = DateUtils.getCurrentDate()
                        compareScheduleEntity = it
                    }
                }


                Result.Success(createList()) as Result<List<ScheduleItem>>
            }
                .startWith(Result.Loading)
                .onErrorReturn { error -> Result.Error(error) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
        }

        override fun fetchScheduleV2Observable(scheduleFetchV2: ScheduleFetchV2): Observable<Result<List<ScheduleItem>>> {
            Log.d("ScheduleViewModelV2", "fromNetwork")
            return Observable.create {
                it.onNext(scheduleFetchV2)
                it.onComplete()
            }.flatMap { schedule ->

                (if (schedule.week == null)
                    scheduleApiRepository.fetchScheduleByGroupNameObservable(schedule.groupName)
                else
                    scheduleApiRepository.fetchScheduleByWeekObservable(
                        compareScheduleEntity!!.group, schedule.week.toString()
                    )
                        ).onErrorResumeNext(Observable.just(schedule)
                        .flatMap {
                            scheduleApiRepository.fetchGroupListObservable(schedule.groupName)
                        }.flatMap {

                            if (schedule.week == null)
                                scheduleApiRepository.fetchScheduleByHtmlObservable(it.choices.first().group)
                            else
                                scheduleApiRepository.fetchScheduleByWeekObservable(
                                    it.choices.first().group,
                                    schedule.week.toString()
                                )

                        })
            }.flatMap {
                if (scheduleFetchV2.week == null) {
                    compareDayOfWeek = CurrentDayOfWeekUtils.getCurrentDayOfWeek()
                    compareDate = DateUtils.getCurrentDate()
                    compareWeek = it.week
                    compareScheduleEntity = it
                } else {
                    compareDayOfWeek = CurrentDayOfWeekUtils.getCurrentDayOfWeek()
                    compareDate = DateUtils.getCurrentDate()
                    compareScheduleEntity = it
                }

                Completable.fromRunnable {
                    scheduleDao.insertSchedule(it.toDbo())
                }.subscribeOn(Schedulers.io()).subscribe()

                Observable.create {emmit ->
                    emmit.onNext(Result.Success(createList()) as Result<List<ScheduleItem>>)
                    emmit.onComplete()
                }
            }
                .startWith(Result.Loading)
                .onErrorReturn { error -> Result.Error(error) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
        }

        override fun reuseListResultObservable(): Observable<Result<List<ScheduleItem>>> {
            if (compareDayOfWeek == null) compareDayOfWeek =
                CurrentDayOfWeekUtils.getCurrentDayOfWeek()
            if (compareDate == null) compareDate = DateUtils.getCurrentDate()
            return Observable.create {
                it.onNext(Result.Success(createList()) as Result<List<ScheduleItem>>)
                it.onComplete()
            }
                .startWith(Result.Loading)
                .onErrorReturn { error -> Result.Error(error) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
        }

        override fun getWeekConfigFromDB(groupName: String): Observable<Result<ScheduleItemListRepository.WeeksConfig>> {
            return scheduleDao.findCompareWeekObservable(groupName)
                .map { it.toEntity() }.map {
                    compareWeek = it.week
                    compareScheduleEntity = it
                    Result.Success(
                        ScheduleItemListRepository.WeeksConfig(
                            it.week,
                            it.weeks
                        )
                    ) as Result<ScheduleItemListRepository.WeeksConfig>
                }.onErrorReturn { error ->
                    Result.Error(error)
                }.startWith(Result.Loading)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
        }

        override fun getScheduleFromDb(
            groupName: String,
            week: Int?
        ): Observable<Result<List<ScheduleItem>>> {
            Log.d("ScheduleViewModelV2", "fromDB")
            return (if (week == null)
                scheduleDao.findCompareWeekObservable(groupName)
            else
                scheduleDao.getScheduleByWeekObservable(groupName, week))
                .map { it.toEntity() }.map {
                    if (week == null) {
                        compareDayOfWeek = CurrentDayOfWeekUtils.getCurrentDayOfWeek()
                        compareDate = DateUtils.getCurrentDate()
                        compareWeek = it.week
                        compareScheduleEntity = it
                    } else {
                        compareDayOfWeek = CurrentDayOfWeekUtils.getCurrentDayOfWeek()
                        compareDate = DateUtils.getCurrentDate()
                        compareScheduleEntity = it
                    }
                    Result.Success(createList()) as Result<List<ScheduleItem>>
                }.onErrorReturn { error ->
                    Result.Error(error)
                }.startWith(Result.Loading)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
        }

        override fun reuseListResultFromDb() {
            if (compareDayOfWeek == null) compareDayOfWeek =
                CurrentDayOfWeekUtils.getCurrentDayOfWeek()
            if (compareDate == null) compareDate = DateUtils.getCurrentDate()
            Completable.fromRunnable {
                scheduleDao.insertSchedule(compareScheduleEntity!!.toDbo())
            }.subscribeOn(Schedulers.io()).subscribe()

        }

        private fun createList(): List<ScheduleItem> {
            val list = mutableListOf<ScheduleItem>()
            for (index in 1..6) {
                val dateCondition = index == compareDayOfWeek &&
                        compareScheduleEntity!!.week == compareWeek &&
                        setupDateFromTable(
                            compareScheduleEntity!!,
                            index
                        ) == compareDate

                if (dateCondition)
                    list.add(
                        ScheduleItem.CurrentDay(
                            dayOfWeekName = DateUtils.getDayOfWeekName(compareScheduleEntity!!.table[index + 1][0]),
                            date = setupDateFromTable(compareScheduleEntity!!, index),
                            lessonsList = compareScheduleEntity!!.table[index + 1],
                            lessonProgress = currentLessonRepository.getCurrentLesson()
                        )
                    )
                else
                    list.add(
                        ScheduleItem.OtherDay(
                            dayOfWeekName = DateUtils.getDayOfWeekName(compareScheduleEntity!!.table[index + 1][0]),
                            date = setupDateFromTable(compareScheduleEntity!!, index),
                            lessonsList = compareScheduleEntity!!.table[index + 1]
                        )
                    )
            }
            return list
        }

        private fun setupDateFromTable(
            scheduleEntity: ScheduleEntity,
            index: Int
        ): String {
            var date = scheduleEntity.table[index + 1][0].subSequence(
                4,
                scheduleEntity.table[index + 1][0].lastIndex + 1
            ).toString()
            if (date[0] == "0"[0])
                date = date.subSequence(1, date.lastIndex + 1).toString()
            date = date.replace("\\s+".toRegex(), " ")
            return date
        }


    }

    sealed class ScheduleFetch {
        data class Init(val groupName: String) : ScheduleFetch()
        data class ByWeek(val groupName: String, val week: Int) : ScheduleFetch()
    }

    data class ScheduleFetchV2(
        val groupName: String,
        val week: Int? = null
    )
}