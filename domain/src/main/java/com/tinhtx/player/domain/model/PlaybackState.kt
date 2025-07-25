// domain/src/main/kotlin/com/tinhtx/player/model/PlaybackState.kt
package com.tinhtx.player.domain.model

data class PlaybackState(
    val currentItem: MediaItem? = null,
    val isPlaying: Boolean = false,
    val playbackPosition: Long = 0L,
    val duration: Long = 0L,
    val playbackSpeed: Float = 1.0f,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val shuffleMode: ShuffleMode = ShuffleMode.OFF,
    val playbackMode: PlaybackMode = PlaybackMode.NORMAL,
    val queue: List<MediaItem> = emptyList(),
    val currentIndex: Int = 0,
    val bufferedPosition: Long = 0L,
    val playbackError: String? = null,
    val progress: Float = 0f
)
