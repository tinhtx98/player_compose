// presentation/src/main/kotlin/com/tinhtx/player/screen/player/MusicPlayerViewModel.kt
package com.tinhtx.player.presentation.screen.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinhtx.player.domain.model.AgeGroup
import com.tinhtx.player.domain.model.EqualizerSettings
import com.tinhtx.player.domain.model.MediaItem
import com.tinhtx.player.domain.model.PlaybackSettings
import com.tinhtx.player.domain.model.PlaybackState
import com.tinhtx.player.domain.model.PrivacySettings
import com.tinhtx.player.domain.model.RepeatMode
import com.tinhtx.player.domain.model.ShuffleMode
import com.tinhtx.player.domain.model.ThemeMode
import com.tinhtx.player.domain.model.UiSettings
import com.tinhtx.player.domain.model.UserPreferences
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
    private val getMediaItemsUseCase: GetMediaItemsUseCase, private val playMediaUseCase: PlayMediaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MusicPlayerUiState())
    val uiState: StateFlow<MusicPlayerUiState> = _uiState.asStateFlow()

    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val _userPreferences = MutableStateFlow(
        UserPreferences(
            ageGroup = AgeGroup.ADULT,
            themeMode = ThemeMode.SYSTEM,
            primaryColor = null,
            language = "vi",
            playbackSettings = PlaybackSettings(),
            equalizerSettings = EqualizerSettings(),
            uiSettings = UiSettings(),
            privacySettings = PrivacySettings()
        )
    )
    val userPreferences: StateFlow<UserPreferences> = _userPreferences.asStateFlow()

    fun loadMediaItem(mediaId: String) {
        viewModelScope.launch {
            getMediaItemsUseCase.getMediaItemById(mediaId).collect { mediaItem ->
                _uiState.value = _uiState.value.copy(currentMediaItem = mediaItem)
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

    fun skipToNext() {
        playMediaUseCase.skipToNext()
    }

    fun skipToPrevious() {
        playMediaUseCase.skipToPrevious()
    }

    fun seekTo(position: Long) {
        playMediaUseCase.seekTo(position)
    }

    fun toggleRepeat() {
        val newMode = when (playbackState.value.repeatMode) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
        playMediaUseCase.setRepeatMode(newMode)
        _playbackState.value = _playbackState.value.copy(repeatMode = newMode)
    }

    fun toggleShuffle() {
        val newMode = if (playbackState.value.shuffleMode == ShuffleMode.OFF) ShuffleMode.ON else ShuffleMode.OFF
        playMediaUseCase.setShuffleMode(newMode)
        _playbackState.value = _playbackState.value.copy(shuffleMode = newMode)
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            uiState.value.currentMediaItem?.let { item ->
                playMediaUseCase.setFavorite(item.id, !item.isFavorite)
            }
        }
    }
}

data class MusicPlayerUiState(
    val currentMediaItem: MediaItem? = null, val lyrics: String = ""
)
