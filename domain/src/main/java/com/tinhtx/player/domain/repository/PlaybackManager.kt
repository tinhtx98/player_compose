// domain/src/main/kotlin/com/tinhtx/player/repository/PlaybackManager.kt
package com.tinhtx.player.domain.repository

import com.tinhtx.player.domain.model.MediaItem
import com.tinhtx.player.domain.model.PlaybackState
import com.tinhtx.player.domain.model.RepeatMode
import com.tinhtx.player.domain.model.ShuffleMode
import kotlinx.coroutines.flow.StateFlow

interface PlaybackManager {
    val playbackState: StateFlow<PlaybackState>
    fun play()
    fun pause()
    fun stop()
    fun seekTo(position: Long)
    fun seekToNext()
    fun seekToPrevious()
    fun setPlaybackSpeed(speed: Float)
    fun setRepeatMode(repeatMode: RepeatMode)
    fun setShuffleMode(shuffleMode: ShuffleMode)
    fun setMediaItems(mediaItems: List<MediaItem>)
    fun addMediaItem(mediaItem: MediaItem)
    fun removeMediaItem(index: Int)
    fun prepare()
    fun release()
    fun playMediaItem(mediaItem: MediaItem)
    fun playMediaItems(mediaItems: List<MediaItem>, startIndex: Int = 0)
}
