// media/src/main/kotlin/com/tinhtx/player/playback/PlaybackService.kt
package com.tinhtx.player.media.playback

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.media3.session.legacy.MediaBrowserCompat
import androidx.media3.session.legacy.MediaBrowserServiceCompat
import androidx.media3.session.legacy.MediaSessionCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var exoPlayerManager: ExoPlayerManager

    private lateinit var mediaSession: MediaSessionCompat

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSessionCompat(this, "TinhTXPlayerService")
        sessionToken = mediaSession.sessionToken

        // Set media session callback
        mediaSession.setCallback(object : MediaSessionCompat.Callback() {
            override fun onPlay() {
                exoPlayerManager.play()
            }

            override fun onPause() {
                exoPlayerManager.pause()
            }

            // Add other callbacks
        })

        // Set pending intent for notification
        mediaSession.setSessionActivity(PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {
        return BrowserRoot("root", null)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        result.sendResult(mutableListOf())
    }

    override fun onDestroy() {
        mediaSession.release()
        exoPlayerManager.release()
        super.onDestroy()
    }
}
