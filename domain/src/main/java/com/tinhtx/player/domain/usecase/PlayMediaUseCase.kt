// domain/src/main/kotlin/com/tinhtx/player/usecase/PlayMediaUseCase.kt
package com.tinhtx.player.domain.usecase

import com.tinhtx.player.domain.model.MediaItem
import com.tinhtx.player.domain.model.RepeatMode
import com.tinhtx.player.domain.model.ShuffleMode
import com.tinhtx.player.domain.repository.MediaRepository
import com.tinhtx.player.media.playback.ExoPlayerManager
import javax.inject.Inject

class PlayMediaUseCase @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val exoPlayerManager: ExoPlayerManager
) {
    fun playMediaItem(mediaItem: MediaItem) {
        // Implementation to play a single media item
    }

    fun playMediaItems(mediaItems: List<MediaItem>, startIndex: Int = 0) {
        // Implementation to play a list of media items
    }

    fun pause() {
        exoPlayerManager.pause()
    }

    fun resume() {
        exoPlayerManager.play()
    }

    fun skipToNext() {
        exoPlayerManager.seekToNext()
    }

    fun skipToPrevious() {
        exoPlayerManager.seekToPrevious()
    }

    fun seekTo(position: Long) {
        exoPlayerManager.seekTo(position)
    }

    fun setRepeatMode(repeatMode: RepeatMode) {
        exoPlayerManager.setRepeatMode(repeatMode)
    }

    fun setShuffleMode(shuffleMode: ShuffleMode) {
        exoPlayerManager.setShuffleMode(shuffleMode)
    }

    fun setPlaybackSpeed(speed: Float) {
        exoPlayerManager.setPlaybackSpeed(speed)
    }

    suspend fun setFavorite(mediaId: String, isFavorite: Boolean) {
        mediaRepository.setFavorite(mediaId, isFavorite)
    }
}
