package com.schedule.scheduleappk.workmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.RxWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

@HiltWorker
class SomeWorkManager @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters
) : RxWorker(context, workerParameters) {
    override fun createWork(): Single<Result> {
//        return Observable.range(1,2).toList().map { Result.success() }
        return Observable.just("").toList().map {
            Log.d("SomeWorkManager", "some work")
            Result.success()
        }
    }


    companion object {
        private val TAG = "SomeWorkManagerTag"

        private fun oneTimeWorkRequest() =
            OneTimeWorkRequestBuilder<SomeWorkManager>().addTag(TAG).build()

        private fun periodicalWorkRequest() =
            PeriodicWorkRequestBuilder<SomeWorkManager>(5, TimeUnit.SECONDS).addTag(TAG).build()

        fun launchOneTimeSomeWorkManager(context: Context): Operation = WorkManager.getInstance(context).enqueueUniqueWork(
            TAG, ExistingWorkPolicy.KEEP, oneTimeWorkRequest()
        )
        fun launchOneTimeSomeWorkManagerr(context: Context) = WorkManager.getInstance(context).getWorkInfoByIdLiveData(
            oneTimeWorkRequest().id
        )

        fun launchPeriodicalWorkRequest(context: Context): Operation = WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            TAG, ExistingPeriodicWorkPolicy.KEEP, periodicalWorkRequest()
        )
    }

}