// media/src/main/kotlin/com/tinhtx/player/playback/MediaNotificationManager.kt
package com.tinhtx.player.media.playback

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerNotificationManager
import com.tinhtx.player.core.common.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val exoPlayer: ExoPlayer
) {

    private val notificationManager = PlayerNotificationManager.Builder(
        context,
        Constants.MEDIA_NOTIFICATION_ID,
        Constants.MEDIA_NOTIFICATION_CHANNEL_ID
    )
        .setChannelDescriptionResourceId(android.R.string.ok) // Use system resource temporarily
        .setMediaDescriptionAdapter(object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun getCurrentContentTitle(player: Player): CharSequence {
                return "Title" // Get from current media
            }

            override fun createCurrentContentIntent(player: Player): PendingIntent? {
                // Return null for now since MainActivity doesn't exist
                return null
            }

            override fun getCurrentContentText(player: Player): CharSequence? {
                return "Artist" // Get from current media
            }

            override fun getCurrentLargeIcon(player: Player, callback: PlayerNotificationManager.BitmapCallback): Bitmap? {
                // Load album art
                return null
            }
        })
        .setNotificationListener(object : PlayerNotificationManager.NotificationListener {
            // Handle notification events
        })
        .build()

    init {
        notificationManager.setPlayer(exoPlayer)
    }

    fun release() {
        notificationManager.setPlayer(null)
    }
}
