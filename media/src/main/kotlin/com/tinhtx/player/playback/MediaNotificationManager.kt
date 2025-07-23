package com.tinhtx.player.playback

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.tinhtx.player.common.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaNotificationManager @Inject constructor(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    fun showNotification(service: MediaSessionService, mediaSession: MediaSession) {
        val notification = createNotification(mediaSession)
        service.startForeground(Constants.NOTIFICATION_ID, notification)
    }

    fun hideNotification() {
        notificationManager.cancel(Constants.NOTIFICATION_ID)
    }

    private fun createNotification(mediaSession: MediaSession): Notification {
        return NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Now Playing")
            .setContentText("Music Player")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                "Media Playback",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Media playback controls"
                setShowBadge(false)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}
