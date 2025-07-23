package com.tinhtx.player.model

data class PlaybackState(
    val isPlaying: Boolean = false,
    val currentMediaItem: MediaItem? = null,
    val currentPosition: Long = 0L,
    val bufferedPosition: Long = 0L,
    val playbackSpeed: Float = 1.0f,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val shuffleModeEnabled: Boolean = false,
    val queue: List<MediaItem> = emptyList(),
    val currentIndex: Int = -1
)
