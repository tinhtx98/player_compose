// data/src/main/kotlin/com/tinhtx/player/local/database/entities/MediaEntity.kt
package com.tinhtx.player.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tinhtx.player.domain.model.MediaType

@Entity(tableName = "media_items")
data class MediaEntity(
    @PrimaryKey val id: String,
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
