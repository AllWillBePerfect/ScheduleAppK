package com.schedulev2.data.repositories

import com.schedulev2.utils.LessonProgress
import javax.inject.Inject

interface CurrentLessonRepository {

    fun getCurrentLesson(): LessonProgress

    class Impl @Inject constructor(private val timeProgressIndicatorRepository: TimeProgressIndicatorRepository) : CurrentLessonRepository {
        override fun getCurrentLesson(): LessonProgress {
            val currentPeriod = timeProgressIndicatorRepository.getCurrentPeriod()
            val subtractBetweenTimePeriods = timeProgressIndicatorRepository.getSubtractBetweenTimePeriods(currentPeriod.periodCode)
            return timeProgressIndicatorRepository.getLessonProgress(subtractBetweenTimePeriods, currentPeriod)
        }
    }
}