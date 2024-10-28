package com.schedulev2.data.repositories

import com.schedulev2.utils.LessonProgress
import com.schedulev2.utils.StudyPeriod
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject


interface TimeProgressIndicatorRepository {
    fun getCurrentPeriod(): StudyPeriod

    fun getSubtractBetweenTimePeriods(periodCode: Int): Int
    fun getElapsedTime(subtractTime: Int, studyPeriod: StudyPeriod): Int
    fun getLessonProgress(subtractTime: Int, studyPeriod: StudyPeriod): LessonProgress

    public class Impl @Inject constructor() : TimeProgressIndicatorRepository {
        override fun getCurrentPeriod(): StudyPeriod {
            val moscowTime = SimpleDateFormat("HH:mm:ss", Locale("ru", "RU"))
            moscowTime.timeZone = TimeZone.getTimeZone("Europe/Moscow")
            return when (moscowTime.format(Date())) {
                in StudyPeriod.FIRST_LESSON.startTime..StudyPeriod.FIRST_LESSON.endTime -> StudyPeriod.FIRST_LESSON
                in StudyPeriod.SECOND_LESSON.startTime..StudyPeriod.SECOND_LESSON.endTime -> StudyPeriod.SECOND_LESSON
                in StudyPeriod.THIRD_LESSON.startTime..StudyPeriod.THIRD_LESSON.endTime -> StudyPeriod.THIRD_LESSON
                in StudyPeriod.FOURTH_LESSON.startTime..StudyPeriod.FOURTH_LESSON.endTime -> StudyPeriod.FOURTH_LESSON
                in StudyPeriod.FIFTH_LESSON.startTime..StudyPeriod.FIFTH_LESSON.endTime -> StudyPeriod.FIFTH_LESSON
                in StudyPeriod.SIXTH_LESSON.startTime..StudyPeriod.SIXTH_LESSON.endTime -> StudyPeriod.SIXTH_LESSON
                in StudyPeriod.SEVENTH_LESSON.startTime..StudyPeriod.SEVENTH_LESSON.endTime -> StudyPeriod.SEVENTH_LESSON

                in StudyPeriod.FIRST_LESSON_BREAK.startTime..StudyPeriod.FIRST_LESSON_BREAK.endTime -> StudyPeriod.FIRST_LESSON_BREAK
                in StudyPeriod.SECOND_LESSON_BREAK.startTime..StudyPeriod.SECOND_LESSON_BREAK.endTime -> StudyPeriod.SECOND_LESSON_BREAK
                in StudyPeriod.THIRD_LESSON_BREAK.startTime..StudyPeriod.THIRD_LESSON_BREAK.endTime -> StudyPeriod.THIRD_LESSON_BREAK
                in StudyPeriod.FOURTH_LESSON_BREAK.startTime..StudyPeriod.FOURTH_LESSON_BREAK.endTime -> StudyPeriod.FOURTH_LESSON_BREAK
                in StudyPeriod.FIFTH_LESSON_BREAK.startTime..StudyPeriod.FIFTH_LESSON_BREAK.endTime -> StudyPeriod.FIFTH_LESSON_BREAK
                in StudyPeriod.SIXTH_LESSON_BREAK.startTime..StudyPeriod.SIXTH_LESSON_BREAK.endTime -> StudyPeriod.SIXTH_LESSON_BREAK

                in StudyPeriod.BEFORE_LESSONS.startTime..StudyPeriod.BEFORE_LESSONS.endTime -> StudyPeriod.BEFORE_LESSONS
                in StudyPeriod.AFTER_LESSONS.startTime..StudyPeriod.AFTER_LESSONS.endTime -> StudyPeriod.AFTER_LESSONS

                else -> throw IllegalStateException("Unhandled study period")
            }
        }

        override fun getSubtractBetweenTimePeriods(periodCode: Int): Int {
            val timeEnd: String = when (periodCode) {
                1 -> StudyPeriod.FIRST_LESSON.endTime
                2 -> StudyPeriod.SECOND_LESSON.endTime
                3 -> StudyPeriod.THIRD_LESSON.endTime
                4 -> StudyPeriod.FOURTH_LESSON.endTime
                5 -> StudyPeriod.FIFTH_LESSON.endTime
                6 -> StudyPeriod.SIXTH_LESSON.endTime
                7 -> StudyPeriod.SEVENTH_LESSON.endTime

                11 -> StudyPeriod.FIRST_LESSON_BREAK.endTime
                12 -> StudyPeriod.SECOND_LESSON_BREAK.endTime
                13 -> StudyPeriod.THIRD_LESSON_BREAK.endTime
                14 -> StudyPeriod.FOURTH_LESSON_BREAK.endTime
                15 -> StudyPeriod.FIFTH_LESSON_BREAK.endTime
                16 -> StudyPeriod.SIXTH_LESSON_BREAK.endTime

                21 -> StudyPeriod.BEFORE_LESSONS.endTime
                22 -> StudyPeriod.AFTER_LESSONS.endTime
                else -> throw IllegalStateException("Wrong lesson number: $periodCode")
            }

            val moscowTime = SimpleDateFormat("HH:mm:ss", Locale("ru", "RU"))
            moscowTime.timeZone = TimeZone.getTimeZone("Europe/Moscow")
            val l1 =
                SimpleDateFormat("HH:mm:ss", Locale("ru", "RU")).parse(moscowTime.format(Date()))
            val l2 = SimpleDateFormat("HH:mm:ss", Locale("ru", "RU")).parse(timeEnd)
            val diff: Long = l2.time - l1.time
            val diffMinutes = diff / (60 * 1000) % 60
            val diffHours = diff / (60 * 60 * 1000) % 24
            return if (diffHours.toInt() != 0) diffMinutes.toInt() + diffHours.toInt() * 60 else diffMinutes.toInt()
        }

        override fun getElapsedTime(subtractTime: Int, studyPeriod: StudyPeriod): Int {
            val result: Int = 100 * (studyPeriod.totalMinutes - subtractTime) / studyPeriod.totalMinutes
            if (result >= 100) return 100
            return result
        }

        override fun getLessonProgress(
            subtractTime: Int,
            studyPeriod: StudyPeriod
        ): LessonProgress {
            val result: Int = 100 * (studyPeriod.totalMinutes - subtractTime) / studyPeriod.totalMinutes
            if (result >= 100) return LessonProgress(studyPeriod, 100)
            return LessonProgress(studyPeriod, result)
        }
    }


}
