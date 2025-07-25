// media/src/main/kotlin/com/tinhtx/player/media/playback/ExoPlayerManager.kt
package com.tinhtx.player.media.playback

import androidx.media3.common.MediaItem as ExoMediaItem
import androidx.media3.common.MediaMetadata.MEDIA_TYPE_VIDEO
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.tinhtx.player.domain.model.MediaItem  // Domain model
import com.tinhtx.player.domain.model.MediaType
import com.tinhtx.player.domain.model.PlaybackState
import com.tinhtx.player.domain.model.RepeatMode
import com.tinhtx.player.domain.model.ShuffleMode
import com.tinhtx.player.domain.repository.PlaybackManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.toString

@Singleton
class ExoPlayerManager @Inject constructor(
    private val exoPlayer: ExoPlayer,
    private val mediaSession: MediaSession
) : PlaybackManager {

    private val _playbackState = MutableStateFlow(PlaybackState())
    override val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val playerListener = @UnstableApi
    object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            updatePlaybackState { it.copy(isPlaying = playbackState == Player.STATE_READY) }
        }

        override fun onMediaItemTransition(mediaItem: ExoMediaItem?, reason: Int) {
            updatePlaybackState {
                it.copy(currentItem = mediaItem?.toDomainModel())  // Sử dụng mapper
            }
        }

        override fun onPositionDiscontinuity(reason: Int) {
            updatePlaybackState { it.copy(playbackPosition = exoPlayer.currentPosition) }
        }

        override fun onPlayerError(error: PlaybackException) {
            updatePlaybackState { it.copy(playbackError = error.message) }
        }
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

    override fun setMediaItems(mediaItems: List<MediaItem>) {
        exoPlayer.setMediaItems(mediaItems.map { ExoMediaItem.fromUri(it.uri) })
        updatePlaybackState {
            it.copy(
                queue = mediaItems,
                currentIndex = 0
            )
        }
    }

    override fun addMediaItem(mediaItem: MediaItem) {
        exoPlayer.addMediaItem(ExoMediaItem.fromUri(mediaItem.uri))
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

    override fun playMediaItem(mediaItem: MediaItem) {
        setMediaItems(listOf(mediaItem))
        prepare()
        play()
    }

    override fun playMediaItems(mediaItems: List<MediaItem>, startIndex: Int) {
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
        updatePlaybackState {
            it.copy(
                currentIndex = exoPlayer.currentMediaItemIndex,
                playbackPosition = exoPlayer.currentPosition,
                bufferedPosition = exoPlayer.bufferedPosition
            )
        }
    }

    // Mapper extension để fix lỗi 'toDomain'
    private fun ExoMediaItem.toDomainModel(): MediaItem? {
        // Giả định metadata chứa thông tin cần thiết; nếu không, fetch từ repository bằng ID
        return MediaItem(
            id = localConfiguration?.tag as? String ?: "",  // Sử dụng tag để lưu ID domain nếu có
            uri = localConfiguration?.uri.toString(),
            displayName = mediaMetadata.displayTitle?.toString() ?: mediaMetadata.title?.toString() ?: "Unknown",
            title = mediaMetadata.title?.toString() ?: "Unknown Title",
            artist = mediaMetadata.artist?.toString() ?: "Unknown Artist",
            album = mediaMetadata.albumTitle?.toString() ?: "Unknown Album",
            duration = 0L,  // Lấy từ ExoPlayer nếu có, hoặc fetch
            size = 0L,  // Fetch nếu cần
            dateAdded = 0L,
            dateModified = 0L,
            mimeType = localConfiguration?.mimeType ?: "",
            albumArtUri = mediaMetadata.artworkUri?.toString(),
            track = mediaMetadata.trackNumber,
            year = mediaMetadata.recordingYear,
            genre = mediaMetadata.genre.toString(),
            bitrate = null,  // Không có trong metadata, fetch nếu cần
            sampleRate = null,
            type = if (mediaMetadata.mediaType == MEDIA_TYPE_VIDEO) MediaType.VIDEO else MediaType.AUDIO,
            path = localConfiguration?.uri?.path ?: "",
            playCount = 0,
            lastPlayed = null,
            isFavorite = false,
            isAvailable = true
        )
    }
}
