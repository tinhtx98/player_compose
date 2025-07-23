package com.tinhtx.player.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_items")
data class MediaEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val uri: String,
    val artworkUri: String? = null,
    val mediaType: String,
    val size: Long = 0,
    val dateAdded: Long = 0,
    val path: String = "",
    val mimeType: String = ""
)
