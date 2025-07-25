package com.tinhtx.player.presentation.screen.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinhtx.player.domain.model.MediaItem
import com.tinhtx.player.domain.model.MediaType
import com.tinhtx.player.domain.model.PlaybackState
import com.tinhtx.player.domain.usecase.PlayMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaybackViewModel @Inject constructor(
    private val playMediaUseCase: PlayMediaUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState = _playbackState.asStateFlow()

    // Thêm flag để check xem đã init chưa
    private var isInitialized = false

    private val stateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            println("DEBUG: PlaybackViewModel received broadcast!")

            val isPlaying = intent?.getBooleanExtra("isPlaying", false) ?: false
            val mediaId = intent?.getStringExtra("mediaId") ?: ""
            val title = intent?.getStringExtra("title") ?: ""
            val artist = intent?.getStringExtra("artist") ?: ""
            val albumArtUri = intent?.getStringExtra("albumArtUri") ?: ""
            val position = intent?.getLongExtra("position", 0L) ?: 0L
            val duration = intent?.getLongExtra("duration", 0L) ?: 0L
            val progress = intent?.getFloatExtra("progress", 0f) ?: 0f

            println("DEBUG: Received broadcast - mediaId: $mediaId, title: $title, isPlaying: $isPlaying")

            // Tạo MediaItem từ broadcast data
            val mediaItem = if (mediaId.isNotEmpty()) {
                MediaItem(
                    id = mediaId,
                    title = title,
                    artist = artist,
                    album = "Unknown Album",
                    duration = duration,
                    uri = "content://media/external/audio/media/$mediaId",
                    albumArtUri = albumArtUri.takeIf { it.isNotEmpty() },
                    type = MediaType.AUDIO,
                    isFavorite = false,
                    displayName = "$title.mp3",
                    size = 5242880L,
                    dateAdded = System.currentTimeMillis(),
                    dateModified = System.currentTimeMillis(),
                    mimeType = "audio/mpeg",
                    track = 1,
                    year = 2024,
                    genre = "Pop",
                    bitrate = 320000,
                    sampleRate = 44100,
                    path = "/storage/emulated/0/Music/$title.mp3"
                )
            } else null

            println("DEBUG: Created MediaItem: ${mediaItem?.title}, currentItem before update: ${_playbackState.value.currentItem?.title}")

            _playbackState.value = _playbackState.value.copy(
                currentItem = mediaItem,
                isPlaying = isPlaying,
                playbackPosition = position,
                duration = duration,
                progress = progress
            )

            println("DEBUG: Updated playback state, currentItem after update: ${_playbackState.value.currentItem?.title}")
        }
    }

    init {
        println("DEBUG: PlaybackViewModel init - registering broadcast receiver")
        registerBroadcastReceiver()

        // Request current state nếu có MusicPlayerViewModel đang chạy
        requestCurrentPlaybackState()

        isInitialized = true
        println("DEBUG: PlaybackViewModel initialized successfully")
    }

    private fun registerBroadcastReceiver() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.registerReceiver(
                    stateReceiver,
                    IntentFilter("PLAYBACK_STATE_UPDATE"),
                    Context.RECEIVER_NOT_EXPORTED
                )
            } else {
                ContextCompat.registerReceiver(
                    context,
                    stateReceiver,
                    IntentFilter("PLAYBACK_STATE_UPDATE"),
                    ContextCompat.RECEIVER_NOT_EXPORTED
                )
            }
            println("DEBUG: PlaybackViewModel broadcast receiver registered successfully")
        } catch (e: Exception) {
            println("DEBUG: Failed to register broadcast receiver: ${e.message}")
        }
    }

    private fun requestCurrentPlaybackState() {
        // Gửi broadcast để request current state từ MusicPlayerViewModel
        val intent = Intent("REQUEST_PLAYBACK_STATE")
        context.sendBroadcast(intent)
        println("DEBUG: Requested current playback state")
    }

    fun play(mediaItem: MediaItem) {
        viewModelScope.launch {
            playMediaUseCase.playMediaItem(mediaItem)
            _playbackState.value = _playbackState.value.copy(
                currentItem = mediaItem,
                isPlaying = true,
                progress = 0f
            )
        }
    }

    fun togglePlayPause() {
        val current = _playbackState.value
        _playbackState.value = current.copy(isPlaying = !current.isPlaying)
    }

    fun stop() {
        _playbackState.value = PlaybackState()
    }

    fun updateProgress(position: Long, duration: Long) {
        val progress = if (duration > 0) position.toFloat() / duration else 0f
        _playbackState.value = _playbackState.value.copy(
            playbackPosition = position,
            duration = duration,
            progress = progress
        )
    }

    fun loadAndPlayMediaItem(mediaId: String) {
        viewModelScope.launch {
            try {
                // Giả sử có cách lấy MediaItem từ mediaId
                // Tạm thời tạo MediaItem dummy để test
                val mediaItem = createDummyMediaItem(mediaId)
                play(mediaItem)
            } catch (e: Exception) {
                println("Error loading media: ${e.message}")
            }
        }
    }

    private fun createDummyMediaItem(mediaId: String): MediaItem {
        return MediaItem(
            id = mediaId,
            title = "Sample Song",
            artist = "Sample Artist",
            album = "Sample Album",
            duration = 240000L, // 4 minutes
            uri = "content://media/external/audio/media/$mediaId",
            albumArtUri = null,
            type = MediaType.AUDIO,
            isFavorite = false,
            displayName = "Sample Song.mp3",
            size = 5242880L,
            dateAdded = System.currentTimeMillis(),
            dateModified = System.currentTimeMillis(),
            mimeType = "audio/mpeg",
            track = 1,
            year = 2024,
            genre = "Pop",
            bitrate = 320000,
            sampleRate = 44100,
            path = "/storage/emulated/0/Music/sample.mp3"
        )
    }

    override fun onCleared() {
        context.unregisterReceiver(stateReceiver)
        super.onCleared()
    }
}
