package com.tinhtx.player.model

data class MediaItem(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val uri: String,
    val artworkUri: String? = null,
    val mediaType: MediaType,
    val size: Long = 0,
    val dateAdded: Long = 0,
    val path: String = "",
    val mimeType: String = ""
)
