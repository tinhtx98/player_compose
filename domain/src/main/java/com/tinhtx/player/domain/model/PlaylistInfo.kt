// domain/src/main/kotlin/com/tinhtx/player/model/PlaylistInfo.kt
package com.tinhtx.player.domain.model

data class PlaylistInfo(
    val id: String,
    val name: String,
    val description: String?,
    val artworkUri: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val trackCount: Int,
    val duration: Long,
    val isUserCreated: Boolean,
    val tracks: List<MediaItem>
)
