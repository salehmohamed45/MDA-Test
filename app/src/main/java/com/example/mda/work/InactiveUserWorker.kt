package com.example.mda.work

// Background worker

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mda.data.SettingsDataStore
import com.example.mda.notifications.NotificationHelper
import kotlinx.coroutines.flow.first

class InactiveUserWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        Log.d("WorkerDebug", "InactiveUserWorker: Starting work")

        return try {
            val settingsDataStore = SettingsDataStore(applicationContext)

            val notificationsEnabled = settingsDataStore.notificationsFlow.first()
            Log.d("WorkerDebug", "Notifications enabled in settings: $notificationsEnabled")

            if (!notificationsEnabled) {
                Log.e("WorkerDebug", "Stopped: User disabled notifications in app settings")
                return Result.success()
            }

            val prefs = applicationContext.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val lastOpen = prefs.getLong("last_open", 0L)

            val hours = (System.currentTimeMillis() - lastOpen) / (1000 * 60 * 60)
            Log.d("WorkerDebug", "Last opened: $hours hours ago")

            if (hours >= 0) {
                Log.d("WorkerDebug", "Condition met! Sending notification...")

                NotificationHelper.sendNotification(
                    applicationContext,
                    "We miss you!",
                    "It's been a while since you've watched anything. Check out popular movies and see what's new."
                )
                Log.d("WorkerDebug", "Notification sent")
            } else {
                Log.d("WorkerDebug", "Condition not met (insufficient hours)")
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("WorkerDebug", "Error in Worker: ${e.message}")
            e.printStackTrace()
            Result.failure()
        }
    }
}
