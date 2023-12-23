package com.bigbigdw.manavara.util

import androidx.work.BackoffPolicy
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bigbigdw.manavara.firebase.FirebaseWorkManager
import java.util.concurrent.TimeUnit

object MiningWorker {
    fun doWorkerPeriodic(
        workManager: WorkManager,
        time: Long,
        timeUnit: TimeUnit? = TimeUnit.HOURS,
        tag: String
    ){

        val inputData = Data.Builder()
            .putString(FirebaseWorkManager.WORKER, tag)
            .build()

        val workRequest =

            PeriodicWorkRequestBuilder<FirebaseWorkManager>(time, timeUnit ?: TimeUnit.HOURS)
                .addTag(tag)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .setInputData(inputData)
                .build()

        workManager.enqueueUniquePeriodicWork(
            tag,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            workRequest
        )
    }

    fun cancelAllWorker(workManager: WorkManager){
        workManager.cancelAllWork()
    }

}