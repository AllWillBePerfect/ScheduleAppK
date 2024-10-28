package com.schedulev2.data.repositories

import com.schedulev2.data.mappers.toDbo
import com.schedulev2.data.mappers.toEntity
import com.schedulev2.database.dao.ScheduleDao
import com.schedulev2.models.ui.ScheduleEntity
import com.schedulev2.utils.Result
import com.schedulev2.utils.ScheduleItem
import com.schedulev2.utils.sources.CurrentDayOfWeekUtils
import com.schedulev2.utils.sources.DateUtils
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Deprecated("Deprecated")
interface ScheduleItemListRepository {


    //    fun createList(): List<ScheduleItem>
    fun createListResultObservable(
        groupName: String,
        week: Int? = null
    ): Observable<Result<List<ScheduleItem>>>

    fun createListResultSingle(
        groupName: String,
        week: Int? = null
    ): Single<Result<List<ScheduleItem>>>

    fun reuseListResult(): Single<Result<List<ScheduleItem>>>
    fun setupCompareWeek(groupName: String): Observable<Result<WeeksConfig>>
    fun setupCompareWeekSingle(groupName: String): Observable<Result<WeeksConfig>>
    fun getCompareWeekObservableFromDB(groupName: String): Observable<Result<WeeksConfig>>

    @Deprecated("Deprecated")
    @Singleton
    class Impl @Inject constructor(
        private val currentLessonRepository: CurrentLessonRepository,
        private val scheduleApiRepository: ScheduleApiRepository,
        private val appConfigRepository: AppConfigRepository,
        private val scheduleDao: ScheduleDao
    ) : ScheduleItemListRepository {

        var compareDayOfWeek: Int? = null
        var compareDate: String? = null
        var compareWeek: Int? = null
        var compareScheduleEntity: ScheduleEntity? = null


        override fun createListResultObservable(
            groupName: String,
            week: Int?
        ): Observable<Result<List<ScheduleItem>>> {

            /*Completable.fromAction(scheduleDao::nukeTable)
                .subscribeOn(Schedulers.io())
                .subscribe()*/

            compareDayOfWeek = CurrentDayOfWeekUtils.getCurrentDayOfWeek()
            compareDate = DateUtils.getCurrentDate()

//            val groupName = appConfigRepository.getSingleGroup().groupName

            return Observable.just(groupName)
                .switchMap { name ->
                    (if (week == null)
                        scheduleApiRepository.fetchScheduleByGroupNameObservable(name)
                    else
                        scheduleApiRepository.fetchScheduleByWeekObservable(
                            compareScheduleEntity!!.group,
                            week.toString()
                        ))
                        .onErrorResumeNext(
                            Observable.just(groupName)
                                .flatMap {
                                    scheduleApiRepository.fetchGroupListObservable(
                                        groupName
                                    )
                                }
                                .flatMap {
                                    if (week == null) {
                                        scheduleApiRepository.fetchScheduleByHtmlObservable(it.choices.first().group)
                                    } else {
                                        scheduleApiRepository.fetchScheduleByWeekObservable(
                                            it.choices.first().group,
                                            week.toString()
                                        )
                                    }
                                }
                        )
                }.flatMap {
                    compareDayOfWeek = CurrentDayOfWeekUtils.getCurrentDayOfWeek()
                    compareDate = DateUtils.getCurrentDate()
                    compareWeek = it.week
                    compareScheduleEntity = it

                    /*Completable.fromRunnable {
                        scheduleDao.insertScheduleSingle(it.toDbo())
                    }.subscribeOn(Schedulers.io()).subscribe()*/

                    Observable.just(Result.Success(createList()) as Result<List<ScheduleItem>>)

                }
                .startWith(Result.Loading)
                .onErrorReturn { error ->
                    Result.Error(error)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())

        }

        override fun createListResultSingle(
            groupName: String,
            week: Int?
        ): Single<Result<List<ScheduleItem>>> {
            compareDayOfWeek = CurrentDayOfWeekUtils.getCurrentDayOfWeek()
            compareDate = DateUtils.getCurrentDate()

//            val groupName = appConfigRepository.getSingleGroup().groupName


            return Single.just(groupName)
                .flatMap { name ->
                    if (week == null)
                        scheduleApiRepository.fetchScheduleByGroupNameSingle(name)
                    else
                        scheduleApiRepository.fetchScheduleByWeekSingle(
                            compareScheduleEntity!!.group,
                            week.toString()
                        )
                        .onErrorResumeNext(
                            Single.just(groupName)
                                .flatMap {
                                    scheduleApiRepository.fetchGroupListSingle(
                                        groupName
                                    ).flatMap {
                                        if (week == null) {
                                            scheduleApiRepository.fetchScheduleByHtmlSingle(it.choices.first().group)
                                        } else {
                                            scheduleApiRepository.fetchScheduleByWeekSingle(
                                                it.choices.first().group,
                                                week.toString()
                                            )
                                        }

                                    }
                                }
                        )
                }
                .map {
                    compareDayOfWeek = CurrentDayOfWeekUtils.getCurrentDayOfWeek()
                    compareDate = DateUtils.getCurrentDate()
                    compareWeek = it.week
                    compareScheduleEntity = it

//                    Completable.fromRunnable {
//                        scheduleDao.insertScheduleSingle(it.toDbo()).subscribeOn(Schedulers.io()).subscribe()
//                    }.subscribeOn(Schedulers.io()).subscribe()

                    Result.Success(createList()) as Result<List<ScheduleItem>>

                }
                .onErrorReturn { error ->
                    Result.Error(error)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
        }

        override fun reuseListResult(): Single<Result<List<ScheduleItem>>> {
            if (compareDayOfWeek == null) compareDayOfWeek =
                CurrentDayOfWeekUtils.getCurrentDayOfWeek()
            if (compareDate == null) compareDate = DateUtils.getCurrentDate()
            return Single
                .just(Result.Success(createList()) as Result<List<ScheduleItem>>)
                .onErrorReturn { error ->
                    Result.Error(error)
                }
        }

        override fun setupCompareWeek(groupName: String): Observable<Result<WeeksConfig>> {
            return Observable.just(groupName)
                .switchMap { name ->
                    scheduleApiRepository.fetchScheduleByGroupNameObservable(name)
                        .onErrorResumeNext(
                            Observable.just(groupName)
                                .flatMap { scheduleApiRepository.fetchGroupListObservable(groupName) }
                                .flatMap { scheduleApiRepository.fetchScheduleByHtmlObservable(it.choices.first().group) }

                        )
                }
                .map {
                    compareWeek = it.week
                    compareScheduleEntity = it
                    /*Completable.fromAction(scheduleDao::deleteCompareTable)
                        .subscribeOn(Schedulers.io()).subscribe()
                    Completable.fromRunnable {
                        scheduleDao.insertSchedule(it.toDbo(true))
                    }.subscribeOn(Schedulers.io()).subscribe()*/
                    Completable.fromRunnable {
                        scheduleDao.insertOrUpdateCompareTable(it.toDbo(true))
                    }.subscribeOn(Schedulers.io()).subscribe()
                    Result.Success(WeeksConfig(compareWeek!!, it.weeks)) as Result<WeeksConfig>

                }
                .startWith(Result.Loading)
                .onErrorReturn { error ->
                    Result.Error(error)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())

        }

        override fun setupCompareWeekSingle(groupName: String): Observable<Result<WeeksConfig>> {
            return scheduleDao.findCompareWeekSingle(groupName).toObservable().map {
                it.toEntity()
            }
                .map {
                    compareWeek = it.week
                    compareScheduleEntity = it
                    Result.Success(WeeksConfig(it.week, it.weeks)) as Result<WeeksConfig>
                }
                .flatMap {
                    scheduleApiRepository.fetchScheduleByGroupNameObservable(groupName)
                        .onErrorResumeNext(
                            Observable.just(groupName)
                                .flatMap { scheduleApiRepository.fetchGroupListObservable(groupName) }
                                .flatMap { scheduleApiRepository.fetchScheduleByHtmlObservable(it.choices.first().group) }

                        )
                }
                .flatMap {
//                    Completable.fromRunnable {
                    scheduleDao.insertOrUpdateCompareTable(it.toDbo(true))
//                    }.subscribeOn(Schedulers.io()).subscribe()

                    compareWeek = it.week
                    compareScheduleEntity = it
                    Observable.just(
                        Result.Success(WeeksConfig(compareWeek!!, it.weeks)) as Result<WeeksConfig>
                    )
                }
                .onErrorReturn { error ->
                    Result.Error(error)
                }
                .startWith(Result.Loading)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
        }

        override fun getCompareWeekObservableFromDB(groupName: String): Observable<Result<WeeksConfig>> {
            return scheduleDao.findCompareWeekObservable(groupName)
                .map { it.toEntity() }.map {
                    compareWeek = it.week
                    compareScheduleEntity = it
                    Result.Success(WeeksConfig(it.week, it.weeks)) as Result<WeeksConfig>
                }.onErrorReturn { error ->
                    Result.Error(error)
                }.startWith(Result.Loading)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
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


    data class WeeksConfig(
        val week: Int,
        val weeks: List<Int>
    )


}



