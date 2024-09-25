package com.example.data.repositories.v2.schedule.repository.impl

import com.example.data.repositories.CurrentLessonRepository
import com.example.data.repositories.ScheduleApiRepository
import com.example.data.repositories.v2.schedule.repository.ScheduleRepository
import com.example.data.repositories.v2.schedule.repository.ScheduleRepository.Companion.createMultipleList
import com.example.models.ui.ScheduleEntity
import com.example.models.ui.v2.schedule.ViewPagerItemDomain
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MultipleImpl @Inject constructor(
    private val currentLessonRepository: CurrentLessonRepository,
    private val scheduleApiRepository: ScheduleApiRepository,
    private val appConfigRepositoryV2: AppConfigRepositoryV2
) : ScheduleRepository {

    private var cacheScheduleEntity: List<ScheduleEntity>? = null

    override fun doFetch(): Single<ScheduleRepository.ScheduleConfig> {
        val groupNameList = appConfigRepositoryV2.getMultipleAppState().groupNames
        val fetchObservable = Observable.fromIterable(groupNameList).flatMap { item ->
            scheduleApiRepository.fetchScheduleByGroupNameObservable(item).onErrorResumeNext(
                Observable.just(item)
                    .flatMap { name -> scheduleApiRepository.fetchGroupListObservable(name) }
                    .map { groupList -> groupList.choices.sortedBy { group -> group.name } }
                    .flatMap { list ->
                        scheduleApiRepository.fetchScheduleByHtmlObservable(
                            (list.find { group -> group.name == item }
                                ?: list.first()).group)
                    }
            )
        }.subscribeOn(Schedulers.io()).observeOn(Schedulers.io())

        val lessonsProgressObservable =
            Single.just(currentLessonRepository.getCurrentLesson()).observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())

        val resultObservable =
            Single.zip(
                fetchObservable.toList(),
                lessonsProgressObservable
            ) { schedule, lessonProgress ->
                cacheScheduleEntity = schedule
                Pair(cacheScheduleEntity!!, lessonProgress)
            }.flatMap {
                Single.just(
                    ScheduleRepository.ScheduleConfig(
                        scheduleList = createMultipleList(
                            it.first,
                            it.second
                        ),
                        weeks = it.first[0].weeks,
                        currentWeek = it.first[0].week
                    )
                )
            }
                .subscribeOn(Schedulers.io())


        return resultObservable
    }

    override fun doFetchWeek(week: String): Single<ScheduleRepository.ScheduleConfig> {
        val htmGroupList = cacheScheduleEntity!!.map { it.group }
        val fetchObservable = Observable.fromIterable(htmGroupList).flatMap { item ->
            scheduleApiRepository.fetchScheduleByWeekObservable(item, week).onErrorResumeNext(
                Observable.just(item)
                    .flatMap { name -> scheduleApiRepository.fetchGroupListObservable(name) }
                    .map { groupList -> groupList.choices.sortedBy { group -> group.name } }
                    .flatMap { list ->
                        scheduleApiRepository.fetchScheduleByWeekObservable(((list.find { group -> group.name == item }
                            ?: list.first()).group), week)
                    }
            )
        }.subscribeOn(Schedulers.io()).observeOn(Schedulers.io())

        val lessonsProgressObservable =
            Single.just(currentLessonRepository.getCurrentLesson()).observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())

        val resultObservable =
            Single.zip(
                fetchObservable.toList(),
                lessonsProgressObservable
            ) { schedule, lessonProgress ->
                cacheScheduleEntity = schedule
                Pair(cacheScheduleEntity!!, lessonProgress)
            }.flatMap {
                Single.just(
                    ScheduleRepository.ScheduleConfig(
                        scheduleList = createMultipleList(
                            it.first,
                            it.second
                        ),
                        weeks = it.first[0].weeks,
                        currentWeek = it.first[0].week
                    )
                )
            }
                .subscribeOn(Schedulers.io())


        return resultObservable
    }

    override fun restore(): List<ViewPagerItemDomain>? {
        cacheScheduleEntity?.let {
            val lessonProgress = currentLessonRepository.getCurrentLesson()
            return createMultipleList(it, lessonProgress)
        }
        return null
    }

    override fun restoreEntity(): ScheduleRepository.ScheduleConfig? {
        cacheScheduleEntity?.let {
            val lessonProgress = currentLessonRepository.getCurrentLesson()
            return ScheduleRepository.ScheduleConfig(
                scheduleList = createMultipleList(it, lessonProgress),
                weeks = it[0].weeks,
                currentWeek = it[0].week
            )
        }
        return null
    }


}