package com.schedulev2.data.repositories

import android.content.Context
import androidx.preference.PreferenceManager
import com.schedulev2.database.dao.ScheduleDao
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Completable
import javax.inject.Inject

interface ClearDataRepository {

    fun clearData(): Completable
    fun saveChanges()

    class Impl @Inject constructor(
        @ApplicationContext private val context: Context,
        private val scheduleDao: ScheduleDao

    ) :
        ClearDataRepository {
        override fun clearData(): Completable {
            return Completable.fromCallable {
                val sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

                sharedPreferences.edit().clear().apply()
                scheduleDao.nukeTable()
            }
        }

        override fun saveChanges() {
            val sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val versionName = packageInfo.versionName
            sharedPreferences.edit().putString("cached_version", versionName).apply()

        }
    }
}