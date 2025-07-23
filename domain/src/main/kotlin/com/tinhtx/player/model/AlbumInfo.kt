package com.tinhtx.player.model

data class AlbumInfo(
    val id: String,
    val name: String,
    val artist: String,
    val artworkUri: String? = null,
    val trackCount: Int = 0,
    val year: Int? = null,
    val duration: Long = 0
)
