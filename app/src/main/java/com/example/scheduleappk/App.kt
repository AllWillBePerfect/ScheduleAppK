package com.example.scheduleappk

import android.app.Application
import androidx.preference.PreferenceManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().clear().apply()
    }
}