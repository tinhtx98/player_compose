// presentation/src/main/kotlin/com/tinhtx/player/screen/player/VideoPlayerViewModel.kt
package com.tinhtx.player.presentation.screen.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import com.tinhtx.player.domain.model.MediaItem
import com.tinhtx.player.domain.model.PlaybackState
import com.tinhtx.player.domain.usecase.GetMediaItemsUseCase
import com.tinhtx.player.domain.usecase.PlayMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val getMediaItemsUseCase: GetMediaItemsUseCase,
    private val playMediaUseCase: PlayMediaUseCase,
    val exoPlayer: ExoPlayer
) : ViewModel() {

    private val _uiState = MutableStateFlow(VideoPlayerUiState())
    val uiState: StateFlow<VideoPlayerUiState> = _uiState.asStateFlow()

    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    fun loadVideoItem(mediaId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            getMediaItemsUseCase.getMediaItemById(mediaId).collect { mediaItem ->
                if (mediaItem != null) {
                    _uiState.value = _uiState.value.copy(
                        currentMediaItem = mediaItem,
                        isLoading = false
                    )
                    playMediaUseCase.playMediaItem(mediaItem)
                    _playbackState.value = _playbackState.value.copy(isPlaying = true)
                }
            }
        }
    }

    fun togglePlayPause() {
        if (playbackState.value.isPlaying) {
            playMediaUseCase.pause()
        } else {
            playMediaUseCase.resume()
        }
        _playbackState.value = _playbackState.value.copy(isPlaying = !playbackState.value.isPlaying)
    }

    fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
        _playbackState.value = _playbackState.value.copy(playbackPosition = position)
    }

    fun toggleFullscreen() {
        // This would be handled by the UI layer
        // ExoPlayer doesn't directly handle fullscreen, it's a UI concern
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}

data class VideoPlayerUiState(
    val currentMediaItem: MediaItem? = null,
    val isLoading: Boolean = false
)
