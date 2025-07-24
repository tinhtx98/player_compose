// presentation/src/main/kotlin/com/tinhtx/player/screen/player/MusicPlayerViewModel.kt
package com.tinhtx.player.presentation.screen.player

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinhtx.player.domain.model.AgeGroup
import com.tinhtx.player.domain.model.EqualizerSettings
import com.tinhtx.player.domain.model.MediaItem
import com.tinhtx.player.domain.model.MediaType
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
import com.tinhtx.player.media.playback.PlaybackService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getMediaItemsUseCase: GetMediaItemsUseCase,
    private val playMediaUseCase: PlayMediaUseCase
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

    init {
        // Start updating playback position
        startPlaybackPositionUpdater()
    }

    private fun startPlaybackPositionUpdater() {
        viewModelScope.launch {
            while (true) {
                if (_playbackState.value.isPlaying) {
                    val currentState = _playbackState.value
                    val newPosition = (currentState.playbackPosition + 1000).coerceAtMost(currentState.duration)

                    _playbackState.value = currentState.copy(
                        playbackPosition = newPosition
                    )

                    // Auto pause when reached end
                    if (newPosition >= currentState.duration && currentState.duration > 0) {
                        _playbackState.value = currentState.copy(
                            isPlaying = false,
                            playbackPosition = currentState.duration
                        )
                    }
                }
                delay(1000) // Update every second
            }
        }
    }

    fun loadAndPlayMediaItem(mediaId: String) {
        viewModelScope.launch {
            try {
                getMediaItemsUseCase.getMediaItemById(mediaId).collect { mediaItem ->
                    if (mediaItem != null) {
                        val lyrics = extractLyricsFromAudioFile(mediaItem)
                        _uiState.value = _uiState.value.copy(
                            currentMediaItem = mediaItem,
                            lyrics = lyrics
                        )
                        playMediaUseCase.playMediaItem(mediaItem)

                        // Start PlaybackService to show notification
                        startPlaybackService()

                        _playbackState.value = _playbackState.value.copy(
                            isPlaying = true,
                            duration = 240000L, // 4 minutes sample duration
                            playbackPosition = 0L
                        )
                    }
                }
            } catch (_: Exception) {
                // If use case fails, create sample data
                loadSampleMediaItem(mediaId)
            }
        }
    }

    private fun loadSampleMediaItem(mediaId: String) {
        val sampleMediaItem = MediaItem(
            id = mediaId,
            uri = "content://media/external/audio/media/$mediaId",
            displayName = "Sample Song.mp3",
            title = "Sample Song",
            artist = "Sample Artist",
            album = "Sample Album",
            duration = 240000L, // 4 minutes
            size = 5242880L, // 5MB
            dateAdded = System.currentTimeMillis(),
            dateModified = System.currentTimeMillis(),
            mimeType = "audio/mpeg",
            albumArtUri = null,
            track = 1,
            year = 2024,
            genre = "Pop",
            bitrate = 320000,
            sampleRate = 44100,
            type = MediaType.AUDIO,
            path = "/storage/emulated/0/Music/sample.mp3",
            isFavorite = false
        )
        
        // Try to extract lyrics from the sample media item
        val lyrics = extractLyricsFromAudioFile(sampleMediaItem)

        _uiState.value = _uiState.value.copy(
            currentMediaItem = sampleMediaItem,
            lyrics = lyrics
        )
        
        // Start PlaybackService to show notification
        startPlaybackService()

        _playbackState.value = _playbackState.value.copy(
            isPlaying = true,
            duration = 240000L,
            playbackPosition = 0L
        )
    }

    private fun startPlaybackService() {
        try {
            val intent = Intent(context, PlaybackService::class.java)
            context.startForegroundService(intent)
        } catch (e: Exception) {
            // Handle service start error
        }
    }

    private fun extractLyricsFromAudioFile(mediaItem: MediaItem): String? {
        return try {
            // Try to extract lyrics from audio file metadata
            // This would typically use a library like JAudioTagger or similar
            // For now, we'll simulate based on file name or path for demonstration

            val fileName = mediaItem.displayName.lowercase()
            val title = mediaItem.title?.lowercase() ?: ""

            // Check if the file name or title suggests it might have lyrics
            // In a real implementation, you would parse the audio file metadata
            when {
                fileName.contains("instrumental") || title.contains("instrumental") -> null
                fileName.contains("karaoke") || title.contains("karaoke") -> null
                mediaItem.path?.endsWith(".mp3") == true -> {
                    // Try to read ID3 tags for lyrics
                    readID3Lyrics(mediaItem.path)
                }
                mediaItem.path?.endsWith(".flac") == true -> {
                    // Try to read FLAC metadata for lyrics
                    readFLACLyrics(mediaItem.path)
                }
                else -> null
            }
        } catch (e: Exception) {
            // If extraction fails, return null (no lyrics)
            null
        }
    }

    private fun readID3Lyrics(filePath: String): String? {
        return try {
            // In a real implementation, you would use a library like JAudioTagger
            // to read ID3 tags from MP3 files
            // For demonstration, we'll return null (no lyrics found)

            // Example implementation would be:
            // val audioFile = AudioFileIO.read(File(filePath))
            // val tag = audioFile.tag
            // return tag?.getFirst(FieldKey.LYRICS)

            null
        } catch (e: Exception) {
            null
        }
    }

    private fun readFLACLyrics(filePath: String): String? {
        return try {
            // In a real implementation, you would use a library like JAudioTagger
            // to read Vorbis comments from FLAC files
            // For demonstration, we'll return null (no lyrics found)

            null
        } catch (e: Exception) {
            null
        }
    }

    fun togglePlayPause() {
        val currentState = _playbackState.value
        val newIsPlaying = !currentState.isPlaying
        
        if (newIsPlaying) {
            playMediaUseCase.resume()
        } else {
            playMediaUseCase.pause()
        }
        
        _playbackState.value = currentState.copy(isPlaying = newIsPlaying)
    }

    fun skipToNext() {
        playMediaUseCase.skipToNext()
        // Reset position for new track
        _playbackState.value = _playbackState.value.copy(
            playbackPosition = 0L
        )
    }

    fun skipToPrevious() {
        playMediaUseCase.skipToPrevious()
        // Reset position for new track
        _playbackState.value = _playbackState.value.copy(
            playbackPosition = 0L
        )
    }

    fun seekTo(position: Long) {
        playMediaUseCase.seekTo(position)
        _playbackState.value = _playbackState.value.copy(
            playbackPosition = position
        )
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
            val currentItem = uiState.value.currentMediaItem
            if (currentItem != null) {
                val updatedItem = currentItem.copy(isFavorite = !currentItem.isFavorite)
                _uiState.value = _uiState.value.copy(currentMediaItem = updatedItem)
                playMediaUseCase.setFavorite(currentItem.id, updatedItem.isFavorite)
            }
        }
    }
}

data class MusicPlayerUiState(
    val currentMediaItem: MediaItem? = null, 
    val lyrics: String? = null
)
