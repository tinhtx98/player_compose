// media/src/main/kotlin/com/tinhtx/player/playback/PlaybackService.kt
package com.tinhtx.player.media.playback

import android.app.PendingIntent
import android.content.Intent
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService : MediaSessionService() {

    @Inject
    lateinit var exoPlayerManager: ExoPlayerManager

    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()

        // Initialize MediaSession with the player from ExoPlayerManager
        mediaSession = MediaSession.Builder(this, exoPlayerManager.getPlayer())
            .setCallback(object : MediaSession.Callback {
                // Handle media session callbacks here
            })
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}
