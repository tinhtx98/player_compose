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

    private val notificationManager = PlayerNotificationManager.Builder(context, Constants.MEDIA_NOTIFICATION_ID, Constants.MEDIA_NOTIFICATION_CHANNEL_ID)
        .setChannelName("TinhTX Player Playback")
        .setChannelDescription("Thông báo phát nhạc")
        .setMediaDescriptionAdapter(object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun getCurrentContentTitle(player: Player): CharSequence {
                return "Title" // Get from current media
            }

            override fun createCurrentContentIntent(player: Player): PendingIntent? {
                return PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
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
