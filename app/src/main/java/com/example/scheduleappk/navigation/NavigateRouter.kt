package com.example.scheduleappk.navigation

import androidx.fragment.app.FragmentActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigateRouter @Inject constructor() : ActivityRequired {

    private var activity: FragmentActivity? = null


    override fun onCreated(activity: FragmentActivity) {
        this.activity = activity
    }

    override fun onStarted() {
    }

    override fun onStopped() {
    }

    override fun onDestroyed() {
    }

    fun getActivity(): FragmentActivity? = activity
    fun requireActivity(): FragmentActivity = activity!!
}