// media/src/main/kotlin/com/tinhtx/player/playback/MediaNotificationManager.kt
package com.tinhtx.player.media.playback

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerNotificationManager
import com.tinhtx.player.core.common.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@UnstableApi
@Singleton
class MediaNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val exoPlayer: ExoPlayer
) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.MEDIA_NOTIFICATION_CHANNEL_ID,
                "Media Playback",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Media playback notifications"
                setShowBadge(false)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private val playerNotificationManager = PlayerNotificationManager.Builder(
        context,
        Constants.MEDIA_NOTIFICATION_ID,
        Constants.MEDIA_NOTIFICATION_CHANNEL_ID
    )
        .setChannelDescriptionResourceId(android.R.string.ok)
        .setMediaDescriptionAdapter(object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun getCurrentContentTitle(player: Player): CharSequence {
                return player.currentMediaItem?.mediaMetadata?.title ?: "Unknown Title"
            }

            override fun createCurrentContentIntent(player: Player): PendingIntent? {
                val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                return intent?.let {
                    PendingIntent.getActivity(
                        context,
                        0,
                        it,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                }
            }

            override fun getCurrentContentText(player: Player): CharSequence? {
                return player.currentMediaItem?.mediaMetadata?.artist ?: "Unknown Artist"
            }

            override fun getCurrentLargeIcon(player: Player, callback: PlayerNotificationManager.BitmapCallback): Bitmap? {
                return null
            }
        })
        .setNotificationListener(object : PlayerNotificationManager.NotificationListener {
            override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
                super.onNotificationCancelled(notificationId, dismissedByUser)
            }

            override fun onNotificationPosted(notificationId: Int, notification: Notification, ongoing: Boolean) {
                super.onNotificationPosted(notificationId, notification, ongoing)
            }
        })
        .build()

    init {
        playerNotificationManager.setPlayer(exoPlayer)
        playerNotificationManager.setPriority(NotificationCompat.PRIORITY_LOW)
        playerNotificationManager.setUseRewindAction(false)
        playerNotificationManager.setUseFastForwardAction(false)
    }

    fun createNotification(): Notification {
        return NotificationCompat.Builder(context, Constants.MEDIA_NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Music Player")
            .setContentText("Ready to play music")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }

    fun release() {
        playerNotificationManager.setPlayer(null)
    }
}
