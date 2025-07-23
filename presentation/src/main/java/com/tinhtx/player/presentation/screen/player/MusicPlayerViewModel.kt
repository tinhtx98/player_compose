package com.tinhtx.player.presentation.screen.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class MusicPlayerViewModel @Inject constructor(
    private val getMediaItemsUseCase: GetMediaItemsUseCase,
    private val playMediaUseCase: PlayMediaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MusicPlayerUiState())
    val uiState: StateFlow<MusicPlayerUiState> = _uiState.asStateFlow()

    fun loadMedia(mediaId: String) {
        viewModelScope.launch {
            getMediaItemsUseCase.getMediaItemById(mediaId).collect { resource ->
                resource.data?.let { mediaItem ->
                    _uiState.value = _uiState.value.copy(currentMedia = mediaItem)
                }
            }
        }
    }

    fun togglePlayPause() {
        viewModelScope.launch {
            val currentMedia = _uiState.value.currentMedia ?: return@launch

            if (_uiState.value.isPlaying) {
                playMediaUseCase.pauseMedia()
                _uiState.value = _uiState.value.copy(isPlaying = false)
            } else {
                playMediaUseCase.playMedia(currentMedia)
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

    fun skipToNext() {
        viewModelScope.launch {
            playMediaUseCase.skipToNext()
        }
    }

    fun skipToPrevious() {
        viewModelScope.launch {
            playMediaUseCase.skipToPrevious()
        }
    }
}

data class MusicPlayerUiState(
    val currentMedia: MediaItem? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val bufferedPosition: Long = 0L
)
