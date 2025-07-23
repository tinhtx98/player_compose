// domain/src/main/kotlin/com/tinhtx/player/model/ArtistInfo.kt
package com.tinhtx.player.domain.model

data class ArtistInfo(
    val id: String,
    val name: String,
    val albumCount: Int,
    val trackCount: Int,
    val albums: List<AlbumInfo>,
    val topTracks: List<MediaItem>
)
