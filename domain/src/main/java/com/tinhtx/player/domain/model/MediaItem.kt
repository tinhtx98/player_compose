// domain/src/main/kotlin/com/tinhtx/player/model/MediaItem.kt
package com.tinhtx.player.domain.model

data class MediaItem(
    val id: String,
    val uri: String,
    val displayName: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val size: Long,
    val dateAdded: Long,
    val dateModified: Long,
    val mimeType: String,
    val albumArtUri: String?,
    val track: Int?,
    val year: Int?,
    val genre: String?,
    val bitrate: Int?,
    val sampleRate: Int?,
    val type: MediaType,
    val path: String,
    val playCount: Int = 0,
    val lastPlayed: Long? = null,
    val isFavorite: Boolean = false,
    val isAvailable: Boolean = true
)
