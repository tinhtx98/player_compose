package com.tinhtx.player.media.playback

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.tinhtx.player.core.common.Constants
import com.tinhtx.player.domain.model.PlaybackState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService : Service() {

    @Inject lateinit var exoPlayer: ExoPlayer
    @Inject lateinit var mediaNotificationManager: MediaNotificationManager

    private val binder = PlaybackBinder()
    private val _playbackState = MutableStateFlow(PlaybackState())

    inner class PlaybackBinder : Binder() {
        fun getService() = this@PlaybackService
    }

    override fun onCreate() {
        super.onCreate()
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                _playbackState.value = _playbackState.value.copy(isPlaying = state == Player.STATE_READY)
                sendStateBroadcast()  // Broadcast state update
            }
            // Các listener khác
        })
        startForeground(Constants.MEDIA_NOTIFICATION_ID, mediaNotificationManager.createNotification())
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onDestroy() {
        exoPlayer.release()
        super.onDestroy()
    }

    private fun sendStateBroadcast() {
        val intent = Intent("PLAYBACK_STATE_UPDATE")
        intent.putExtra("isPlaying", _playbackState.value.isPlaying)
        sendBroadcast(intent)
    }

    // Hàm gọi từ ViewModel để play/pause/seek
    fun play() { exoPlayer.play() }
    fun pause() { exoPlayer.pause() }
    fun seekTo(position: Long) { exoPlayer.seekTo(position) }
}
