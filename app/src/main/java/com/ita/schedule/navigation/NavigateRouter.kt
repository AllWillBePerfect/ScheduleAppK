package com.ita.schedule.navigation

import androidx.fragment.app.FragmentActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigateRouter @Inject constructor() : ActivityRequired {

    private var activity: FragmentActivity? = null


    override fun onCreated(activity: FragmentActivity) {
        this.activity = activity
    }

    override fun onStarted(activity: FragmentActivity) {

    }

    override fun onResumed(activity: FragmentActivity) {
//        this.activity = activity
    }

    override fun onPause() {
//        this.activity = null
    }

    override fun onStopped() {
    }

    override fun onDestroyed() {
        this.activity = null
    }

    fun getActivity(): FragmentActivity? = activity
    fun requireActivity(): FragmentActivity = activity!!
}