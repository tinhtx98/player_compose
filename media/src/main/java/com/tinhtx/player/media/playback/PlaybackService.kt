// media/src/main/kotlin/com/tinhtx/player/playback/PlaybackService.kt
package com.tinhtx.player.media.playback

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.legacy.MediaSessionCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class PlaybackService : Service() {

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var mediaNotificationManager: MediaNotificationManager

    private val binder = PlaybackBinder()
    private var mediaSession: MediaSessionCompat? = null

    inner class PlaybackBinder : Binder() {
        fun getService(): PlaybackService = this@PlaybackService
        fun getPlayer(): ExoPlayer = exoPlayer
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize media session
        mediaSession = MediaSessionCompat(this, "PlaybackService")

        // Start foreground notification
        startForegroundNotification()
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun startForegroundNotification() {
        // Start as foreground service with notification
        val notification = mediaNotificationManager.createNotification()
        startForeground(1001, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaNotificationManager.release()
        mediaSession?.release()
        mediaSession = null
    }
}
