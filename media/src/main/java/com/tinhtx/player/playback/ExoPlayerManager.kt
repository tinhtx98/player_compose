package com.tinhtx.player.playback

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.datasource.DefaultDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExoPlayerManager @Inject constructor(
    private val context: Context
) {
    private val _exoPlayer = ExoPlayer.Builder(context).build()
    val exoPlayer: ExoPlayer = _exoPlayer

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    init {
        _exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    _duration.value = _exoPlayer.duration
                }
            }
        })

        // Update current position periodically
        startPositionUpdates()
    }

    fun playMedia(uri: String) {
        val mediaItem = MediaItem.fromUri(uri)
        val mediaSource = createMediaSource(mediaItem)
        _exoPlayer.setMediaSource(mediaSource)
        _exoPlayer.prepare()
        _exoPlayer.play()
    }

    fun pause() {
        _exoPlayer.pause()
    }

    fun resume() {
        _exoPlayer.play()
    }

    fun stop() {
        _exoPlayer.stop()
    }

    fun seekTo(position: Long) {
        _exoPlayer.seekTo(position)
    }

    fun skipToNext() {
        if (_exoPlayer.hasNextMediaItem()) {
            _exoPlayer.seekToNext()
        }
    }

    fun skipToPrevious() {
        if (_exoPlayer.hasPreviousMediaItem()) {
            _exoPlayer.seekToPrevious()
        }
    }

    fun setPlaylist(mediaItems: List<MediaItem>) {
        _exoPlayer.setMediaItems(mediaItems)
        _exoPlayer.prepare()
    }

    fun release() {
        _exoPlayer.release()
    }

    private fun createMediaSource(mediaItem: MediaItem): MediaSource {
        val dataSourceFactory = DefaultDataSource.Factory(context)
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)
    }

    private fun startPositionUpdates() {
        // This would typically use a coroutine or timer to update position
        // For simplicity, we'll use a basic approach
        object : Runnable {
            override fun run() {
                if (_exoPlayer.isPlaying) {
                    _currentPosition.value = _exoPlayer.currentPosition
                }
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(this, 1000)
            }
        }.run()
    }
}
