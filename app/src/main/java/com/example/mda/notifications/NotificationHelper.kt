package com.example.mda.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.mda.R
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random

object NotificationHelper {
    private const val CHANNEL_ID = "movies_channel"
    private const val CHANNEL_NAME = "Movies Updates"

    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannelIfNeeded(ctx: Context) {
        val manager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (manager.getNotificationChannel(CHANNEL_ID) == null) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "News regarding trending movies and suggestions"
            }
            manager.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNotification(
        ctx: Context,
        title: String,
        body: String,
        imageUrl: String? = null,
        tapIntent: Intent? = null
    ) {
        createChannelIfNeeded(ctx)
        val manager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val pending = tapIntent?.let {
            PendingIntent.getActivity(
                ctx, Random.nextInt(), it,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val builder = NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setColor(ContextCompat.getColor(ctx, R.color.teal_200))
            .setAutoCancel(true)
            .setContentIntent(pending)

        if (imageUrl != null) {
            val bitmap = getBitmapFromUrl(imageUrl)
            if (bitmap != null) {
                builder.setLargeIcon(bitmap)
                builder.setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .bigLargeIcon(null as Bitmap?)
                        .setSummaryText(body)
                )
            }
        } else {
            builder.setStyle(NotificationCompat.BigTextStyle().bigText(body))
        }

        manager.notify(Random.nextInt(1000, 9999), builder.build())
    }

    private fun getBitmapFromUrl(src: String): Bitmap? {
        return try {
            val url = URL(src)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}