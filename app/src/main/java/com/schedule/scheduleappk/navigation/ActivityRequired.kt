package com.schedule.scheduleappk.navigation

import androidx.fragment.app.FragmentActivity

interface ActivityRequired {

    fun onCreated(activity: FragmentActivity)

    fun onStarted(activity: FragmentActivity)

    fun onResumed(activity: FragmentActivity)

    fun onPause()

    fun onStopped()

    fun onDestroyed()
}