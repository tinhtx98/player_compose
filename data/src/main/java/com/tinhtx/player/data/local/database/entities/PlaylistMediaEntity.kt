// data/src/main/kotlin/com/tinhtx/player/local/database/entities/PlaylistMediaEntity.kt
package com.tinhtx.player.data.local.database.entities

import androidx.room.Entity

@Entity(
    tableName = "playlist_media",
    primaryKeys = ["playlistId", "mediaId"]
)
data class PlaylistMediaEntity(
    val playlistId: String,
    val mediaId: String,
    val position: Int,
    val addedAt: Long
)
