package com.schedulev2.scheduleappk.di

import com.schedulev2.scheduleappk.navigation.ActivityRequired
import com.schedulev2.scheduleappk.navigation.NavigateRouter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
class ModuleProvides {

    @Provides
    @ElementsIntoSet
    fun provideActivityRequiredSet(
    ): Set<@JvmSuppressWildcards ActivityRequired> = hashSetOf<ActivityRequired>()

    @Provides
    @IntoSet
    fun provideRouterAsActivityRequired(
        router: NavigateRouter,
    ): ActivityRequired {
        return router
    }
}