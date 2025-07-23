package com.tinhtx.player.model

data class ArtistInfo(
    val id: String,
    val name: String,
    val albumCount: Int = 0,
    val trackCount: Int = 0,
    val artworkUri: String? = null
)
