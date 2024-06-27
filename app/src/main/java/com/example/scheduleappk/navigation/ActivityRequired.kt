package com.example.scheduleappk.navigation

import androidx.fragment.app.FragmentActivity

interface ActivityRequired {

    fun onCreated(activity: FragmentActivity)

    fun onStarted()

    fun onStopped()

    fun onDestroyed()
}