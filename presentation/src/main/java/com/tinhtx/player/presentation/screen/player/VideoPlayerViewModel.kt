package com.tinhtx.player.presentation.screen.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import com.tinhtx.player.model.MediaItem
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

    fun loadVideo(mediaId: String) {
        viewModelScope.launch {
            getMediaItemsUseCase.getMediaItemById(mediaId).collect { resource ->
                resource.data?.let { mediaItem ->
                    _uiState.value = _uiState.value.copy(currentVideo = mediaItem)
                    // Load video into ExoPlayer
                    playMediaUseCase.playMedia(mediaItem)
                }
            }
        }
    }

    fun togglePlayPause() {
        viewModelScope.launch {
            if (_uiState.value.isPlaying) {
                playMediaUseCase.pauseMedia()
                _uiState.value = _uiState.value.copy(isPlaying = false)
            } else {
                val currentVideo = _uiState.value.currentVideo ?: return@launch
                playMediaUseCase.playMedia(currentVideo)
                _uiState.value = _uiState.value.copy(isPlaying = true)
            }
        }
    }

    fun seekTo(position: Long) {
        viewModelScope.launch {
            playMediaUseCase.seekTo(position)
            _uiState.value = _uiState.value.copy(currentPosition = position)
        }
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}

data class VideoPlayerUiState(
    val currentVideo: MediaItem? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val bufferedPosition: Long = 0L
)
