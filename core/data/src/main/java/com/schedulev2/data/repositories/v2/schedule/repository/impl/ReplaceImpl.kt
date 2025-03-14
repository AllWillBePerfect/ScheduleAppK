package com.schedulev2.data.repositories.v2.schedule.repository.impl

import com.schedulev2.data.repositories.CurrentLessonRepository
import com.schedulev2.data.repositories.ScheduleApiRepository
import com.schedulev2.data.repositories.v2.schedule.repository.ScheduleRepository
import com.schedulev2.data.repositories.v2.schedule.repository.ScheduleRepository.Companion.createReplaceList
import com.schedulev2.models.ui.ScheduleEntity
import com.schedulev2.models.ui.v2.schedule.ViewPagerItemDomain
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReplaceImpl @Inject constructor(
    private val currentLessonRepository: CurrentLessonRepository,
    private val scheduleApiRepository: ScheduleApiRepository,
    private val appConfigRepositoryV2: AppConfigRepositoryV2
) : ScheduleRepository {

    private var cacheScheduleEntity: ScheduleEntity? = null
    private var cacheScheduleEntities: List<ScheduleEntity>? = null

    private lateinit var groupNameList: List<String>
    private lateinit var replaceDays: List<Int>

    override fun doFetch(): Single<ScheduleRepository.ScheduleConfig> {
        val state = appConfigRepositoryV2.getReplaceAppState()
        groupNameList = listOf(state.groupName, state.vpkName)
        replaceDays = state.replaceDays

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
                /*var scheduleCopy = schedule[0].copy()
                for (i in replaceDays) {
                    val table = scheduleCopy.table.toMutableList()
                    table[i + 1] = schedule[1].table[i + 1]
                    scheduleCopy = scheduleCopy.copy(table = table)
                }*/

                var scheduleCopy = schedule[0].copy()
                val tableCopy = scheduleCopy.table.toMutableList()

                replaceDays.forEach { dayIndex ->
                    val updatedRow = tableCopy[dayIndex + 1].toMutableList()
                    val secondRow = schedule[1].table[dayIndex + 1]

                    updatedRow.forEachIndexed { index, value ->
                        if (value.contains("впк", ignoreCase = true)) {
                            updatedRow[index] = secondRow[index]
                        }
                    }

                    tableCopy[dayIndex + 1] = updatedRow
                }

                scheduleCopy = scheduleCopy.copy(table = tableCopy)

                /*val groupSchedule = schedule[0].copy()
                val vpkSchedule = schedule[1].copy()
                replaceDays.forEach {
                    val dayIndex = it + 1
                    val updatedRow = groupSchedule.table[dayIndex].toMutableList()
                    val secondRow = vpkSchedule.table[dayIndex]
                    updatedRow.forEachIndexed { index, value ->
                        if (value.contains("впк", ignoreCase = true)) {
                            updatedRow[index] = secondRow[index]
                        }
                    }

                }*/
                cacheScheduleEntity = scheduleCopy
                cacheScheduleEntities = schedule
                Pair(cacheScheduleEntity!!, lessonProgress)
            }.flatMap {
                Single.just(
                    ScheduleRepository.ScheduleConfig(
                        scheduleList = createReplaceList(
                            it.first,
                            it.second,
                            groupNameList[0],
                            groupNameList[1],
                            replaceDays
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
        val htmGroupList = cacheScheduleEntities!!.map { it.group }
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
                /*var scheduleCopy = schedule[0].copy()
                for (i in replaceDays) {
                    val table = scheduleCopy.table.toMutableList()
                    table[i + 1] = schedule[1].table[i + 1]
                    scheduleCopy = scheduleCopy.copy(table = table)
                }*/

                var scheduleCopy = schedule[0].copy()
                val tableCopy = scheduleCopy.table.toMutableList()

                replaceDays.forEach { dayIndex ->
                    val updatedRow = tableCopy[dayIndex + 1].toMutableList()
                    val secondRow = schedule[1].table[dayIndex + 1]

                    updatedRow.forEachIndexed { index, value ->
                        if (value.contains("впк", ignoreCase = true)) {
                            updatedRow[index] = secondRow[index]
                        }
                    }

                    tableCopy[dayIndex + 1] = updatedRow
                }

                scheduleCopy = scheduleCopy.copy(table = tableCopy)

                cacheScheduleEntity = scheduleCopy
                cacheScheduleEntities = schedule
                Pair(cacheScheduleEntity!!, lessonProgress)
            }.flatMap {
                Single.just(
                    ScheduleRepository.ScheduleConfig(
                        scheduleList = createReplaceList(
                            it.first,
                            it.second,
                            groupNameList[0],
                            groupNameList[1],
                            replaceDays
                        ),
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
            return createReplaceList(it, lessonProgress, groupNameList[0], groupNameList[1], replaceDays)
        }
        return null
    }

    override fun restoreEntity(): ScheduleRepository.ScheduleConfig? {
        cacheScheduleEntity?.let {
            val lessonProgress = currentLessonRepository.getCurrentLesson()
            return ScheduleRepository.ScheduleConfig(
                scheduleList = createReplaceList(
                    it,
                    lessonProgress,
                    groupNameList[0],
                    groupNameList[1],
                    replaceDays
                ),
                weeks = it.weeks,
                currentWeek = it.week
            )
        }
        return null
    }


}