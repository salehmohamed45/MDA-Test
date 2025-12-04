package com.example.mda

import android.app.Application
import android.os.Build
import androidx.work.*
import com.example.mda.work.InactiveUserWorker
import com.example.mda.work.SuggestedMovieWorker
import com.example.mda.work.TrendingReminderWorker
import java.util.concurrent.TimeUnit

class MdaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupBackgroundWorkers()
    }

    private fun setupBackgroundWorkers() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workManager = WorkManager.getInstance(this)

        val inactiveUserRequest = PeriodicWorkRequestBuilder<InactiveUserWorker>(
            12, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "InactiveUserWork",
            ExistingPeriodicWorkPolicy.KEEP,
            inactiveUserRequest
        )

        val suggestedMovieRequest = PeriodicWorkRequestBuilder<SuggestedMovieWorker>(
            24, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "SuggestedMovieWork",
            ExistingPeriodicWorkPolicy.KEEP,
            suggestedMovieRequest
        )

        val trendingRequest = PeriodicWorkRequestBuilder<TrendingReminderWorker>(
            1, TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "TrendingWork",
            ExistingPeriodicWorkPolicy.KEEP,
            trendingRequest
        )
    }
}