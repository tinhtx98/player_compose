// media/src/main/kotlin/com/tinhtx/player/playback/ExoPlayerManager.kt
package com.tinhtx.player.media.playback

import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.tinhtx.player.domain.model.PlaybackState
import com.tinhtx.player.domain.model.RepeatMode
import com.tinhtx.player.domain.model.ShuffleMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExoPlayerManager @Inject constructor(
    private val exoPlayer: ExoPlayer,
    private val mediaSession: MediaSession
) {

    private val _playbackState = MutableStateFlow(
        PlaybackState()
    )

    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            updatePlaybackState { it.copy(isPlaying = isPlaying) }
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            // Handle state changes
        }

        override fun onPlayerError(error: PlaybackException) {
            updatePlaybackState {
                it.copy(playbackError = error.message)
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            updateCurrentMediaItem()
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            val domainRepeatMode = when (repeatMode) {
                Player.REPEAT_MODE_OFF -> RepeatMode.OFF
                Player.REPEAT_MODE_ONE -> RepeatMode.ONE
                Player.REPEAT_MODE_ALL -> RepeatMode.ALL
                else -> RepeatMode.OFF
            }
            updatePlaybackState { it.copy(repeatMode = domainRepeatMode) }
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            val shuffleMode = if (shuffleModeEnabled) ShuffleMode.ON else ShuffleMode.OFF
            updatePlaybackState { it.copy(shuffleMode = shuffleMode) }
        }
    }

    init {
        exoPlayer.addListener(playerListener)
    }

    fun play() {
        exoPlayer.play()
    }

    fun pause() {
        exoPlayer.pause()
    }

    fun stop() {
        exoPlayer.stop()
    }

    fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
    }

    fun seekToNext() {
        exoPlayer.seekToNext()
    }

    fun seekToPrevious() {
        exoPlayer.seekToPrevious()
    }

    fun setPlaybackSpeed(speed: Float) {
        exoPlayer.setPlaybackSpeed(speed)
        updatePlaybackState { it.copy(playbackSpeed = speed) }
    }

    fun setRepeatMode(repeatMode: RepeatMode) {
        val exoRepeatMode = when (repeatMode) {
            RepeatMode.OFF -> Player.REPEAT_MODE_OFF
            RepeatMode.ONE -> Player.REPEAT_MODE_ONE
            RepeatMode.ALL -> Player.REPEAT_MODE_ALL
        }
        exoPlayer.repeatMode = exoRepeatMode
    }

    fun setShuffleMode(shuffleMode: ShuffleMode) {
        exoPlayer.shuffleModeEnabled = shuffleMode == ShuffleMode.ON
    }

    fun setMediaItems(mediaItems: List<MediaItem>) {
        exoPlayer.setMediaItems(mediaItems)
        updatePlaybackState {
            it.copy(
                queue = mediaItems.map { /* Convert to domain MediaItem */ },
                currentIndex = 0
            )
        }
    }

    fun addMediaItem(mediaItem: MediaItem) {
        exoPlayer.addMediaItem(mediaItem)
    }

    fun removeMediaItem(index: Int) {
        exoPlayer.removeMediaItem(index)
    }

    fun prepare() {
        exoPlayer.prepare()
    }

    fun release() {
        exoPlayer.removeListener(playerListener)
        exoPlayer.release()
        mediaSession.release()
    }

    private fun updatePlaybackState(update: (PlaybackState) -> PlaybackState) {
        _playbackState.value = update(_playbackState.value)
    }

    private fun updateCurrentMediaItem() {
        val currentMediaItem = exoPlayer.currentMediaItem
        // Convert ExoPlayer MediaItem to domain MediaItem
        // and update playback state
        updatePlaybackState {
            it.copy(
                currentIndex = exoPlayer.currentMediaItemIndex,
                playbackPosition = exoPlayer.currentPosition,
                bufferedPosition = exoPlayer.bufferedPosition
            )
        }
    }

    fun playMediaItem(mediaItem: MediaItem) {
        // Implementation to play a single item
        setMediaItems(listOf(MediaItem.fromUri(mediaItem.uri)))
        prepare()
        play()
    }

    fun playMediaItems(mediaItems: List<MediaItem>, startIndex: Int = 0) {
        // Implementation to play list
        setMediaItems(mediaItems.map { MediaItem.fromUri(it.uri) })
        exoPlayer.seekTo(startIndex, 0L)
        prepare()
        play()
    }
}
