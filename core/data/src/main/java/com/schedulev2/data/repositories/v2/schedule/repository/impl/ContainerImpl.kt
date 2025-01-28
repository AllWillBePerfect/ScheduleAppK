package com.schedulev2.data.repositories.v2.schedule.repository.impl

import com.schedulev2.data.repositories.di.MultipleImplementation
import com.schedulev2.data.repositories.di.ReplaceImplementation
import com.schedulev2.data.repositories.di.SingleImplementation
import com.schedulev2.data.repositories.v2.schedule.repository.ScheduleRepository
import com.schedulev2.models.sharpref.v2.AppStateV2
import com.schedulev2.models.ui.v2.schedule.ViewPagerItemDomain
import io.reactivex.Single
import javax.inject.Inject

class ContainerImpl @Inject constructor(
    @SingleImplementation private val singleImpl: ScheduleRepository,
    @ReplaceImplementation private val replaceImpl: ScheduleRepository,
    @MultipleImplementation private val multipleImpl: ScheduleRepository,
    private val appConfigRepositoryV2: AppConfigRepositoryV2
): ScheduleRepository {

    private fun getImpl() = when (appConfigRepositoryV2.getAppState()) {
        is AppStateV2.Single -> singleImpl
        is AppStateV2.Replace -> replaceImpl
        is AppStateV2.Multiple -> multipleImpl
        is AppStateV2.Unselected -> throw IllegalStateException("Unselected app state in repository")
    }

    override fun doFetch(): Single<ScheduleRepository.ScheduleConfig> = getImpl().doFetch()

    override fun doFetchWeek(week: String): Single<ScheduleRepository.ScheduleConfig> = getImpl().doFetchWeek(week)

    override fun restore(): List<ViewPagerItemDomain>? = getImpl().restore()

    override fun restoreEntity(): ScheduleRepository.ScheduleConfig? = getImpl().restoreEntity()
}