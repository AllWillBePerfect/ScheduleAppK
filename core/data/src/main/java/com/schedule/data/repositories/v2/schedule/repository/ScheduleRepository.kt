package com.schedule.data.repositories.v2.schedule.repository

import com.schedule.models.ui.ScheduleEntity
import com.schedule.models.ui.v2.schedule.TimetableItemDomain
import com.schedule.models.ui.v2.schedule.ViewPagerItemDomain
import com.schedule.utils.LessonProgress
import com.schedule.utils.StudyPeriod
import com.schedule.utils.sources.CurrentDayOfWeekUtils
import com.schedule.utils.sources.DateUtils
import io.reactivex.Single

interface ScheduleRepository {

    data class ScheduleConfig(
        val scheduleList: List<ViewPagerItemDomain>,
        val weeks: List<Int>,
        val currentWeek: Int
    )

    fun doFetch(): Single<ScheduleConfig>
    fun doFetchWeek(week: String): Single<ScheduleConfig>
    fun restore(): List<ViewPagerItemDomain>?
    fun restoreEntity(): ScheduleConfig?

    companion object {

        fun createList(
            scheduleEntity: ScheduleEntity,
            lessonProgress: LessonProgress
        ): List<ViewPagerItemDomain> {
            val list = mutableListOf<ViewPagerItemDomain>()
            for (index in 1..6) {
                val dateCondition = index == CurrentDayOfWeekUtils.getCurrentDayOfWeek() &&
//                        scheduleEntity.week == scheduleEntity.week &&
                        setupDateFromTable(
                            scheduleEntity,
                            index
                        ) == DateUtils.getCurrentDate()

                if (dateCondition)
                    list.add(
                        ViewPagerItemDomain.RecyclerViewCurrentDay(
                            lessons = createCurrentDaysList(
                                scheduleEntity.table[index + 1],
                                lessonProgress,
                                scheduleEntity.name,
                                false
                            )
                        )
                    )
                else
                    list.add(
                        ViewPagerItemDomain.RecyclerViewDay(
                            lessons = createSimpleDaysList(
                                scheduleEntity.table[index + 1],
                                scheduleEntity.name,
                                false
                            )
                        )
                    )
            }
            return list
        }

        fun createReplaceList(
            scheduleEntity: ScheduleEntity,
            lessonProgress: LessonProgress,
            scheduleName: String,
            vpkName: String,
            replaceDays: List<Int>
        ): List<ViewPagerItemDomain> {
            val list = mutableListOf<ViewPagerItemDomain>()
            for (index in 1..6) {
                val dateCondition = index == CurrentDayOfWeekUtils.getCurrentDayOfWeek() &&
//                        scheduleEntity.week == scheduleEntity.week &&
                        setupDateFromTable(
                            scheduleEntity,
                            index
                        ) == DateUtils.getCurrentDate()

                if (dateCondition)
                    list.add(
                        ViewPagerItemDomain.RecyclerViewCurrentDay(
                            lessons = createCurrentDaysList(
                                scheduleEntity.table[index + 1],
                                lessonProgress,
                                if (replaceDays.contains(index)) vpkName else scheduleName,
                                replaceDays.contains(index)

                            )
                        )
                    )
                else
                    list.add(
                        ViewPagerItemDomain.RecyclerViewDay(
                            lessons = createSimpleDaysList(
                                scheduleEntity.table[index + 1],
                                if (replaceDays.contains(index)) vpkName else scheduleName,
                                replaceDays.contains(index)
                            )
                        )
                    )
            }
            return list
        }

        fun createMultipleList(
            scheduleEntitiesList: List<ScheduleEntity>,
            lessonProgress: LessonProgress
        ): List<ViewPagerItemDomain> {
            val list = mutableListOf<ViewPagerItemDomain>()
            for (index in 1..6) {
                val dateCondition = index == CurrentDayOfWeekUtils.getCurrentDayOfWeek() &&
//                        scheduleEntitiesList[0].week == scheduleEntitiesList[0].week &&
                        setupDateFromTable(
                            scheduleEntitiesList[0],
                            index
                        ) == DateUtils.getCurrentDate()

                if (dateCondition)
                    list.add(
                        ViewPagerItemDomain.RecyclerViewCurrentDay(
                            lessons = scheduleEntitiesList.map { item ->
                                createCurrentDaysList(
                                    item.table[index + 1],
                                    lessonProgress,
                                    item.name,
                                    true
                                )
                            }.flatten()
                        )
                    )
                else
                    list.add(
                        ViewPagerItemDomain.RecyclerViewDay(
                            lessons = scheduleEntitiesList.map { item ->
                                createSimpleDaysList(
                                    item.table[index + 1],
                                    item.name,
                                    true
                                )
                            }.flatten()
                        )
                    )
            }
            return list
        }


        private fun createCurrentDaysList(
            lessons: List<String>,
            lessonProgress: LessonProgress,
            groupName: String,
            isTitleEnabled: Boolean
        ): List<TimetableItemDomain> {
            val list = mutableListOf<TimetableItemDomain>()
            list.add(
                TimetableItemDomain.TitleCurrent(
                    date = setupDateFromTable(lessons[0]),
                    dayOfWeekName = DateUtils.getDayOfWeekName(lessons[0]),
                    groupName = groupName,
                    isTitleEnabled = isTitleEnabled
                )
            )
            for (index in 1..7) {
                list.add(
                    TimetableItemDomain.Lesson(
                        time = generateTimeForLessons(index),
                        lessonName = lessons[index],
                        getLessonContentType(lessons[index])
                    )
                )
            }
            when (lessonProgress.studyPeriod) {
                StudyPeriod.FIRST_LESSON -> {
                    list[1] = createCurrentTimetableItemLesson(
                        lesson = list[1] as TimetableItemDomain.Lesson,
                        progressValue = lessonProgress.progressValue
                    )
                }

                StudyPeriod.SECOND_LESSON -> {
                    list[2] = createCurrentTimetableItemLesson(
                        lesson = list[2] as TimetableItemDomain.Lesson,
                        progressValue = lessonProgress.progressValue
                    )
                }

                StudyPeriod.THIRD_LESSON -> {
                    list[3] = createCurrentTimetableItemLesson(
                        lesson = list[3] as TimetableItemDomain.Lesson,
                        progressValue = lessonProgress.progressValue
                    )
                }

                StudyPeriod.FOURTH_LESSON -> {
                    list[4] = createCurrentTimetableItemLesson(
                        lesson = list[4] as TimetableItemDomain.Lesson,
                        progressValue = lessonProgress.progressValue
                    )
                }

                StudyPeriod.FIFTH_LESSON -> {
                    list[5] = createCurrentTimetableItemLesson(
                        lesson = list[5] as TimetableItemDomain.Lesson,
                        progressValue = lessonProgress.progressValue
                    )
                }

                StudyPeriod.SIXTH_LESSON -> {
                    list[6] = createCurrentTimetableItemLesson(
                        lesson = list[6] as TimetableItemDomain.Lesson,
                        progressValue = lessonProgress.progressValue
                    )
                }

                StudyPeriod.SEVENTH_LESSON -> {
                    list[7] = createCurrentTimetableItemLesson(
                        lesson = list[7] as TimetableItemDomain.Lesson,
                        progressValue = lessonProgress.progressValue
                    )
                }

                StudyPeriod.FIRST_LESSON_BREAK -> {
                    list.add(
                        2, createCurrentTimetableItemBreak(
                            time = StudyPeriod.FIRST_LESSON_BREAK.fullTime,
                            lessonName = "Перерыв перед 2-ой парой",
                            progressValue = lessonProgress.progressValue
                        )
                    )
                }

                StudyPeriod.SECOND_LESSON_BREAK -> {
                    list.add(
                        3, createCurrentTimetableItemBreak(
                            time = StudyPeriod.SECOND_LESSON_BREAK.fullTime,
                            lessonName = "Перерыв перед 3-ей парой",
                            progressValue = lessonProgress.progressValue
                        )
                    )
                }

                StudyPeriod.THIRD_LESSON_BREAK -> {
                    list.add(
                        4, createCurrentTimetableItemBreak(
                            time = StudyPeriod.THIRD_LESSON_BREAK.fullTime,
                            lessonName = "Перерыв перед 4-ой парой",
                            progressValue = lessonProgress.progressValue
                        )
                    )
                }

                StudyPeriod.FOURTH_LESSON_BREAK -> {
                    list.add(
                        5, createCurrentTimetableItemBreak(
                            time = StudyPeriod.FOURTH_LESSON_BREAK.fullTime,
                            lessonName = "Перерыв перед 5-ой парой",
                            progressValue = lessonProgress.progressValue
                        )
                    )
                }

                StudyPeriod.FIFTH_LESSON_BREAK -> {
                    list.add(
                        6, createCurrentTimetableItemBreak(
                            time = StudyPeriod.FIFTH_LESSON_BREAK.fullTime,
                            lessonName = "Перерыв перед 6-ой парой",
                            progressValue = lessonProgress.progressValue
                        )
                    )
                }

                StudyPeriod.SIXTH_LESSON_BREAK -> {
                    list.add(
                        7, createCurrentTimetableItemBreak(
                            time = StudyPeriod.SIXTH_LESSON_BREAK.fullTime,
                            lessonName = "Перерыв перед 7-ой парой",
                            progressValue = lessonProgress.progressValue
                        )
                    )
                }

                StudyPeriod.BEFORE_LESSONS -> {
                    list.add(
                        1, createCurrentTimetableItemBreak(
                            time = StudyPeriod.BEFORE_LESSONS.fullTime,
                            lessonName = "До начала учебного дня еще есть время, пока живем",
                            progressValue = lessonProgress.progressValue
                        )
                    )
                }

                StudyPeriod.AFTER_LESSONS -> {
                    list.add(
                        createCurrentTimetableItemBreak(
                            time = StudyPeriod.AFTER_LESSONS.fullTime,
                            lessonName = "Вы пережили этот день",
                            progressValue = lessonProgress.progressValue
                        )
                    )
                }
            }
            return list
        }

        private fun createSimpleDaysList(
            lessons: List<String>,
            groupName: String,
            isTitleEnabled: Boolean
        ): List<TimetableItemDomain> {
            val list = mutableListOf<TimetableItemDomain>()
            list.add(
                TimetableItemDomain.Title(
                    date = setupDateFromTable(lessons[0]),
                    dayOfWeekName = DateUtils.getDayOfWeekName(lessons[0]),
                    groupName = groupName,
                    isTitleEnabled = isTitleEnabled
                )
            )
            for (index in 1..7) {
                list.add(
                    TimetableItemDomain.Lesson(
                        time = generateTimeForLessons(index),
                        lessonName = lessons[index],
                        lessonContentTypeDomain = getLessonContentType(lessons[index])
                    )
                )
            }
            return list
        }

        private fun getLessonContentType(lessonContent: String): TimetableItemDomain.Companion.ContentTypeDomain {
            return when {
                lessonContent.isEmpty() -> TimetableItemDomain.Companion.ContentTypeDomain.NONE
                Regex("LMS").containsMatchIn(lessonContent) -> TimetableItemDomain.Companion.ContentTypeDomain.ONLINE
                else -> TimetableItemDomain.Companion.ContentTypeDomain.OFFLINE
            }
        }


        private fun createCurrentTimetableItemLesson(
            lesson: TimetableItemDomain.Lesson,
            progressValue: Int
        ) = TimetableItemDomain.LessonCurrent(
            time = lesson.time,
            lessonName = lesson.lessonName,
            progressValue = progressValue,
            lessonContentTypeDomain = getLessonContentType(lesson.lessonName)
        )

        private fun createCurrentTimetableItemBreak(
            time: String,
            lessonName: String,
            progressValue: Int
        ) = TimetableItemDomain.Break(
            time = time,
            lessonName = lessonName,
            progressValue = progressValue
        )

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

        private fun setupDateFromTable(
            list: List<String>
        ): String {
            var date = list[0].subSequence(
                4,
                list[0].lastIndex + 1
            ).toString()
            if (date[0] == "0"[0])
                date = date.subSequence(1, date.lastIndex + 1).toString()
            date = date.replace("\\s+".toRegex(), " ")
            return date
        }

        private fun setupDateFromTable(
            dateString: String
        ): String {
            var date = dateString.subSequence(
                4,
                dateString.lastIndex + 1
            ).toString()
            if (date[0] == "0"[0])
                date = date.subSequence(1, date.lastIndex + 1).toString()
            date = date.replace("\\s+".toRegex(), " ")
            return date
        }

        private fun generateTimeForLessons(index: Int): String {
            return when (index) {
                StudyPeriod.FIRST_LESSON.periodCode -> StudyPeriod.FIRST_LESSON.fullTime
                StudyPeriod.SECOND_LESSON.periodCode -> StudyPeriod.SECOND_LESSON.fullTime
                StudyPeriod.THIRD_LESSON.periodCode -> StudyPeriod.THIRD_LESSON.fullTime
                StudyPeriod.FOURTH_LESSON.periodCode -> StudyPeriod.FOURTH_LESSON.fullTime
                StudyPeriod.FIFTH_LESSON.periodCode -> StudyPeriod.FIFTH_LESSON.fullTime
                StudyPeriod.SIXTH_LESSON.periodCode -> StudyPeriod.SIXTH_LESSON.fullTime
                StudyPeriod.SEVENTH_LESSON.periodCode -> StudyPeriod.SEVENTH_LESSON.fullTime
                else -> throw IllegalArgumentException("Wrong lesson period")
            }
        }
    }

}