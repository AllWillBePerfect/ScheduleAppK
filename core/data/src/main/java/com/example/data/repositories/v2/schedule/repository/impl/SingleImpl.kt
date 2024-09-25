package com.example.data.repositories.v2.schedule.repository.impl

import com.example.data.repositories.CurrentLessonRepository
import com.example.data.repositories.ScheduleApiRepository
import com.example.data.repositories.v2.schedule.repository.ScheduleRepository
import com.example.data.repositories.v2.schedule.repository.ScheduleRepository.Companion.createList
import com.example.models.ui.ScheduleEntity
import com.example.models.ui.v2.schedule.ViewPagerItemDomain
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SingleImpl @Inject constructor(
    private val currentLessonRepository: CurrentLessonRepository,
    private val scheduleApiRepository: ScheduleApiRepository,
    private val appConfigRepositoryV2: AppConfigRepositoryV2
) : ScheduleRepository {

    private var cacheScheduleEntity: ScheduleEntity? = null

    override fun doFetch(): Single<ScheduleRepository.ScheduleConfig> {
        val groupName = appConfigRepositoryV2.getSingleAppState().groupName
        val fetchObservable = Single.just(groupName).flatMap {
            scheduleApiRepository.fetchScheduleByGroupNameSingle(it).onErrorResumeNext {
                Single.just(groupName)
                    .flatMap { name -> scheduleApiRepository.fetchGroupListSingle(name) }
                    .map { groupList -> groupList.choices.sortedBy { group -> group.name } }
                    .flatMap { list ->
                        scheduleApiRepository.fetchScheduleByHtmlSingle(
                            (list.find { group -> group.name == groupName }
                                ?: list.first()).group)
                    }
            }
        }.subscribeOn(Schedulers.io()).observeOn(Schedulers.io())

        val lessonsProgressObservable =
            Single.just(currentLessonRepository.getCurrentLesson()).observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())

        val resultObservable =
            Single.zip(fetchObservable, lessonsProgressObservable) { schedule, lessonProgress ->
                cacheScheduleEntity = schedule
                Pair(cacheScheduleEntity!!, lessonProgress)
            }.flatMap {

                Single.just(
                    ScheduleRepository.ScheduleConfig(
                        scheduleList = createList(
                            it.first,
                            it.second,
                        ),
                        weeks = it.first.weeks,
                        currentWeek = it.first.week
                    )
                )
            }
                .subscribeOn(Schedulers.io())


        return resultObservable
    }

    override fun doFetchWeek(week: String): Single<ScheduleRepository.ScheduleConfig> {
        val htmGroup = cacheScheduleEntity!!.group
        val fetchObservable = Single.just(htmGroup).flatMap {
            scheduleApiRepository.fetchScheduleByWeekSingle(htmGroup, week).onErrorResumeNext {
                Single.just(htmGroup)
                    .flatMap { name -> scheduleApiRepository.fetchGroupListSingle(name) }
                    .map { groupList -> groupList.choices.sortedBy { group -> group.name } }
                    .flatMap { list ->
                        scheduleApiRepository.fetchScheduleByWeekSingle(((list.find { group -> group.name == htmGroup }
                            ?: list.first()).group), week)
                    }
            }
        }.subscribeOn(Schedulers.io()).observeOn(Schedulers.io())

        val lessonsProgressObservable =
            Single.just(currentLessonRepository.getCurrentLesson()).observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())

        val resultObservable =
            Single.zip(fetchObservable, lessonsProgressObservable) { schedule, lessonProgress ->
                cacheScheduleEntity = schedule
                Pair(cacheScheduleEntity!!, lessonProgress)
            }.flatMap {
                Single.just(
                    ScheduleRepository.ScheduleConfig(
                        scheduleList = createList(it.first, it.second),
                        weeks = it.first.weeks,
                        currentWeek = it.first.week
                    )
                )
            }
                .subscribeOn(Schedulers.io())


        return resultObservable
    }

    override fun restore(): List<ViewPagerItemDomain>? {
        cacheScheduleEntity?.let {
            val lessonProgress = currentLessonRepository.getCurrentLesson()
            return createList(it, lessonProgress)
        }
        return null
    }

    override fun restoreEntity(): ScheduleRepository.ScheduleConfig? {
        cacheScheduleEntity?.let {
            val lessonProgress = currentLessonRepository.getCurrentLesson()
            return ScheduleRepository.ScheduleConfig(
                scheduleList = createList(it, lessonProgress),
                weeks = it.weeks,
                currentWeek = it.week
            )
        }
        return null
    }


}