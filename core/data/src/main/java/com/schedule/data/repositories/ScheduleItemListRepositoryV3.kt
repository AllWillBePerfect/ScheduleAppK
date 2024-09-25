package com.schedule.data.repositories

import com.schedule.data.mappers.toDbo
import com.schedule.data.mappers.toEntity
import com.schedule.database.dao.ScheduleDao
import com.schedule.models.ui.ScheduleEntity
import com.schedule.models.ui.ScheduleEntityStatus
import com.schedule.utils.Result
import com.schedule.utils.ScheduleItem
import com.schedule.utils.ScheduleResult
import com.schedule.utils.sources.CurrentDayOfWeekUtils
import com.schedule.utils.sources.DateUtils
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

interface ScheduleItemListRepositoryV3 {

    fun fetchWeeksConfig(groupName: String): Observable<ScheduleResult<ScheduleWeeksConfigContainer>>
    fun fetchSchedule(
        groupName: String,
        week: Int? = null
    ): Observable<ScheduleResult<ScheduleListContainer>>

    fun getWeeksConfigFromDB(groupName: String): Observable<ScheduleResult<ScheduleWeeksConfigContainer>>
    fun getScheduleFromDb(
        groupName: String,
        week: Int? = null
    ): Observable<ScheduleResult<ScheduleListContainer>>

    fun reuseList(): Observable<Result<ScheduleListContainer>>


    @Singleton
    class Impl @Inject constructor(
        private val currentLessonRepository: CurrentLessonRepository,
        private val scheduleApiRepository: ScheduleApiRepository,
        private val appConfigRepository: AppConfigRepository,
        private val scheduleDao: ScheduleDao
    ) : ScheduleItemListRepositoryV3 {

        var compareDayOfWeek: Int? = null
        var compareDate: String? = null
        var compareWeek: Int? = null
        var compareScheduleEntity: ScheduleEntity? = null

        override fun fetchWeeksConfig(groupName: String): Observable<ScheduleResult<ScheduleWeeksConfigContainer>> {

            return Single.just(groupName).toObservable()
                        .flatMap {
                            scheduleApiRepository.fetchGroupListDTO(groupName).toObservable()
                        }
                        .map { it.copy(choices = it.choices.sortedBy {group -> group.name }) }
                        .flatMap {
                            scheduleApiRepository.fetchScheduleByHtmlDTO((it.choices.find { it.name == groupName } ?: it.choices.first()).group)
                                .toObservable()
                        }.onErrorResumeNext(scheduleApiRepository.fetchScheduleByGroupNameDTO(groupName).toObservable())
                .map { it.toEntity(status = ScheduleEntityStatus.NETWORK) }.flatMap {

                    compareWeek = it.week
                    compareScheduleEntity = it

                    Completable.fromRunnable {
                        scheduleDao.insertOrUpdateCompareTable(it.toDbo(true))
                    }.subscribeOn(Schedulers.io()).subscribe()

                    Completable.fromRunnable {
                        scheduleDao.insertSchedule(it.toDbo())
                    }.subscribeOn(Schedulers.io()).subscribe()

                    Single.just(
                        ScheduleResult.SuccessFromNetwork(
                            ScheduleWeeksConfigContainer(
                                weeksConfig = WeeksConfig(
                                    it.week,
                                    it.weeks
                                ),
                                scheduleStatus = ScheduleListContainer.ScheduleStatus(
                                    cacheTime = it.cacheTime,
                                    cacheDate = it.cacheDate,
                                    status = it.status,
                                    week = it.week
                                )
                            )
                        ) as ScheduleResult<ScheduleWeeksConfigContainer>
                    ).toObservable()

                }
                .startWith(ScheduleResult.Loading)
                .onErrorReturn { error -> ScheduleResult.Error(error) }
                .subscribeOn(Schedulers.io())


        }

        override fun fetchSchedule(
            groupName: String,
            week: Int?
        ): Observable<ScheduleResult<ScheduleListContainer>> {
            return (if (week == null)
                scheduleApiRepository.fetchScheduleByGroupNameDTO(groupName).toObservable()
            else
                scheduleApiRepository.fetchScheduleByWeekDTO(
                    compareScheduleEntity!!.group, week.toString()
                ).toObservable()
                    ).onErrorResumeNext(
                    scheduleApiRepository.fetchGroupListDTO(groupName).toObservable()
                        .map {it.copy(choices = it.choices.sortedBy {group -> group.name })}
                        .flatMap {
                        if (week == null)
                            scheduleApiRepository.fetchScheduleByHtmlDTO((it.choices.find { it.name == groupName } ?: it.choices.first()).group)
                                .toObservable()
                        else
                            scheduleApiRepository.fetchScheduleByWeekDTO(
                                it.choices.first().group,
                                week.toString()
                            ).toObservable()

                    })
                .map { it.toEntity(status = ScheduleEntityStatus.NETWORK) }.flatMap {
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

                    Completable.fromRunnable {
                        scheduleDao.insertSchedule(it.toDbo())
                    }.subscribeOn(Schedulers.io()).subscribe()

                    Single.just(
                        ScheduleResult.SuccessFromNetwork(
                            ScheduleListContainer(
                                list = createList(),
                                scheduleStatus = ScheduleListContainer.ScheduleStatus(
                                    cacheTime = it.cacheTime,
                                    cacheDate = it.cacheDate,
                                    status = it.status,
                                    week = it.week
                                )
                            )
                        ) as ScheduleResult<ScheduleListContainer>
                    )
                        .toObservable()
                }
                .startWith(ScheduleResult.Loading)
                .onErrorReturn { error -> ScheduleResult.Error(error) }
                .subscribeOn(Schedulers.io())
        }

        override fun getWeeksConfigFromDB(groupName: String): Observable<ScheduleResult<ScheduleWeeksConfigContainer>> {
            return scheduleDao.findCompareWeekSingle(groupName).toObservable().map { it.toEntity() }
                .flatMap {
                    compareWeek = it.week
                    compareScheduleEntity = it
                    Single.just(
                        ScheduleResult.SuccessFromDB(
                            ScheduleWeeksConfigContainer(
                                weeksConfig = WeeksConfig(
                                    it.week,
                                    it.weeks
                                ),
                                scheduleStatus = ScheduleListContainer.ScheduleStatus(
                                    cacheTime = it.cacheTime,
                                    cacheDate = it.cacheDate,
                                    status = it.status,
                                    week = it.week
                                )
                            )
                        ) as ScheduleResult<ScheduleWeeksConfigContainer>
                    ).toObservable()
                }.onErrorReturn { error ->
                    ScheduleResult.Error(error)
                }.startWith(ScheduleResult.Loading)
                .subscribeOn(Schedulers.io())


        }

        override fun getScheduleFromDb(
            groupName: String,
            week: Int?
        ): Observable<ScheduleResult<ScheduleListContainer>> {
            return (if (week == null)
                scheduleDao.findCompareWeekSingle(groupName).toObservable()
            else
                scheduleDao.getScheduleByWeekSingle(groupName, week).toObservable())
                .map { it.toEntity() }.flatMap {
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
                    Single.just(
                        ScheduleResult.SuccessFromNetwork(
                            ScheduleListContainer(
                                list = createList(),
                                scheduleStatus = ScheduleListContainer.ScheduleStatus(
                                    cacheTime = it.cacheTime,
                                    cacheDate = it.cacheDate,
                                    status = it.status,
                                    week = it.week
                                )
                            )
                        ) as ScheduleResult<ScheduleListContainer>
                    )
                        .toObservable()
                }.onErrorReturn { error ->
                    ScheduleResult.Error(error)
                }.startWith(ScheduleResult.Loading)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
        }

        override fun reuseList(): Observable<Result<ScheduleListContainer>> {
            if (compareDayOfWeek == null) compareDayOfWeek =
                CurrentDayOfWeekUtils.getCurrentDayOfWeek()
            if (compareDate == null) compareDate = DateUtils.getCurrentDate()

            return Single.create {
                if (compareScheduleEntity == null)
                    it.onError(Throwable("not fetched"))
                it.onSuccess(Result.Success(
                    ScheduleListContainer(
                        list = createList(),
                        scheduleStatus = ScheduleListContainer.ScheduleStatus(
                            cacheTime = compareScheduleEntity!!.cacheTime,
                            cacheDate = compareScheduleEntity!!.cacheDate,
                            status = compareScheduleEntity!!.status,
                            week = compareScheduleEntity!!.week
                        )
                    )
                ) as Result<ScheduleListContainer>)
            }
                .toObservable()
                .startWith(Result.Loading)
                .onErrorReturn { error -> Result.Error(error) }
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

    data class ScheduleWeeksConfigContainer(
        val weeksConfig: WeeksConfig,
        val scheduleStatus: ScheduleListContainer.ScheduleStatus
    )

    data class ScheduleListContainer(
        val list: List<ScheduleItem>,
        val scheduleStatus: ScheduleStatus
    ) {
        data class ScheduleStatus(
            val cacheTime: String,
            val cacheDate: String,
            val status: ScheduleEntityStatus,
            val week: Int
        )
    }
}