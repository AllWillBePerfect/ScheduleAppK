package com.example.data

import com.example.data.repositories.TimeProgressIndicatorRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import org.junit.Assert.*


@RunWith(JUnit4::class)
class TimeProgressIndicatorRepositoryTest {

    lateinit var timeProgressIndicatorRepository: TimeProgressIndicatorRepository

    @Before
    fun setup() {
        timeProgressIndicatorRepository = TimeProgressIndicatorRepository.Impl()
    }

    @Test
    fun getCurrentPeriod() {
        assertEquals("period", timeProgressIndicatorRepository.getCurrentPeriod())
    }

    @Test
    fun getSubtractBetweenTimePeriods() {
        val studyPeriod = timeProgressIndicatorRepository.getCurrentPeriod()
        assertEquals("subtractTime", timeProgressIndicatorRepository.getSubtractBetweenTimePeriods(studyPeriod.periodCode))
    }

    @Test
    fun getElapsedTime() {
        val studyPeriod = timeProgressIndicatorRepository.getCurrentPeriod()
        val subtractTime = timeProgressIndicatorRepository.getSubtractBetweenTimePeriods(studyPeriod.periodCode)
        assertEquals("progressValue", timeProgressIndicatorRepository.getElapsedTime(subtractTime, studyPeriod))
    }

    @Test
    fun getLessonProgress() {
        val studyPeriod = timeProgressIndicatorRepository.getCurrentPeriod()
        val subtractTime = timeProgressIndicatorRepository.getSubtractBetweenTimePeriods(studyPeriod.periodCode)
        assertEquals("progressValue", timeProgressIndicatorRepository.getLessonProgress(subtractTime, studyPeriod))
    }
}