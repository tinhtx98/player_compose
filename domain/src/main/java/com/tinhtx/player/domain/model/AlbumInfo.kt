// domain/src/main/kotlin/com/tinhtx/player/model/AlbumInfo.kt
package com.tinhtx.player.domain.model

data class AlbumInfo(
    val id: String,
    val name: String,
    val artist: String,
    val artworkUri: String?,
    val trackCount: Int,
    val year: Int?,
    val genre: String?,
    val duration: Long,
    val tracks: List<MediaItem>
)
