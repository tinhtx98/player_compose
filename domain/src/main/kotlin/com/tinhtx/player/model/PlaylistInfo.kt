package com.tinhtx.player.model

data class PlaylistInfo(
    val id: String,
    val name: String,
    val description: String = "",
    val artworkUri: String? = null,
    val trackCount: Int = 0,
    val duration: Long = 0,
    val dateCreated: Long = System.currentTimeMillis(),
    val dateModified: Long = System.currentTimeMillis()
)
