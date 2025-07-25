// domain/src/main/kotlin/com/tinhtx/player/usecase/PlayMediaUseCase.kt
package com.tinhtx.player.domain.usecase

import com.tinhtx.player.domain.model.MediaItem
import com.tinhtx.player.domain.model.RepeatMode
import com.tinhtx.player.domain.model.ShuffleMode
import com.tinhtx.player.domain.repository.MediaRepository
import com.tinhtx.player.domain.repository.PlaybackManager
import javax.inject.Inject

class PlayMediaUseCase @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val playbackManager: PlaybackManager
) {
    fun playMediaItem(mediaItem: MediaItem) {
        playbackManager.playMediaItem(mediaItem)
    }

    fun playMediaItems(mediaItems: List<MediaItem>, startIndex: Int = 0) {
        playbackManager.playMediaItems(mediaItems, startIndex)
    }

    fun pause() {
        playbackManager.pause()
    }

    fun resume() {
        playbackManager.play()
    }

    fun stop() {
        playbackManager.stop()
    }

    fun skipToNext() {
        playbackManager.seekToNext()
    }

    fun skipToPrevious() {
        playbackManager.seekToPrevious()
    }

    fun seekTo(position: Long) {
        playbackManager.seekTo(position)
    }

    fun setRepeatMode(repeatMode: RepeatMode) {
        playbackManager.setRepeatMode(repeatMode)
    }

    fun setShuffleMode(shuffleMode: ShuffleMode) {
        playbackManager.setShuffleMode(shuffleMode)
    }

    fun setPlaybackSpeed(speed: Float) {
        playbackManager.setPlaybackSpeed(speed)
    }

    suspend fun setFavorite(mediaId: String, isFavorite: Boolean) {
        mediaRepository.setFavorite(mediaId, isFavorite)
    }
}
