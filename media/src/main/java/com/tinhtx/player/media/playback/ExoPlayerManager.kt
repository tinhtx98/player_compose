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
import com.tinhtx.player.domain.repository.PlaybackManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExoPlayerManager @Inject constructor(
    private val exoPlayer: ExoPlayer,
    private val mediaSession: MediaSession
) : PlaybackManager {

    private val _playbackState = MutableStateFlow(
        PlaybackState()
    )

    override val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val playerListener = object : Player.Listener {
        // (giữ nguyên như code ban đầu)
    }

    init {
        exoPlayer.addListener(playerListener)
    }

    override fun play() {
        exoPlayer.play()
    }

    override fun pause() {
        exoPlayer.pause()
    }

    override fun stop() {
        exoPlayer.stop()
    }

    override fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
    }

    override fun seekToNext() {
        exoPlayer.seekToNext()
    }

    override fun seekToPrevious() {
        exoPlayer.seekToPrevious()
    }

    override fun setPlaybackSpeed(speed: Float) {
        exoPlayer.setPlaybackSpeed(speed)
        updatePlaybackState { it.copy(playbackSpeed = speed) }
    }

    override fun setRepeatMode(repeatMode: RepeatMode) {
        val exoRepeatMode = when (repeatMode) {
            RepeatMode.OFF -> Player.REPEAT_MODE_OFF
            RepeatMode.ONE -> Player.REPEAT_MODE_ONE
            RepeatMode.ALL -> Player.REPEAT_MODE_ALL
        }
        exoPlayer.repeatMode = exoRepeatMode
    }

    override fun setShuffleMode(shuffleMode: ShuffleMode) {
        exoPlayer.shuffleModeEnabled = shuffleMode == ShuffleMode.ON
    }

    override fun setMediaItems(mediaItems: List<com.tinhtx.player.domain.model.MediaItem>) {
        exoPlayer.setMediaItems(mediaItems.map { MediaItem.fromUri(it.uri) })
        updatePlaybackState {
            it.copy(
                queue = mediaItems,
                currentIndex = 0
            )
        }
    }

    override fun addMediaItem(mediaItem: com.tinhtx.player.domain.model.MediaItem) {
        exoPlayer.addMediaItem(MediaItem.fromUri(mediaItem.uri))
    }

    override fun removeMediaItem(index: Int) {
        exoPlayer.removeMediaItem(index)
    }

    override fun prepare() {
        exoPlayer.prepare()
    }

    override fun release() {
        exoPlayer.removeListener(playerListener)
        exoPlayer.release()
        mediaSession.release()
    }

    override fun playMediaItem(mediaItem: com.tinhtx.player.domain.model.MediaItem) {
        setMediaItems(listOf(mediaItem))
        prepare()
        play()
    }

    override fun playMediaItems(mediaItems: List<com.tinhtx.player.domain.model.MediaItem>, startIndex: Int) {
        setMediaItems(mediaItems)
        exoPlayer.seekTo(startIndex, 0L)
        prepare()
        play()
    }

    fun getPlayer(): ExoPlayer = exoPlayer

    private fun updatePlaybackState(update: (PlaybackState) -> PlaybackState) {
        _playbackState.value = update(_playbackState.value)
    }

    private fun updateCurrentMediaItem() {
        val currentMediaItem = exoPlayer.currentMediaItem
        // Convert back to domain MediaItem (you may need a mapper or ID based lookup)
        updatePlaybackState {
            it.copy(
                currentIndex = exoPlayer.currentMediaItemIndex,
                playbackPosition = exoPlayer.currentPosition,
                bufferedPosition = exoPlayer.bufferedPosition
            )
        }
    }
}
