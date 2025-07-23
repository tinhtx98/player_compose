// data/src/main/kotlin/com/tinhtx/player/local/database/entities/PlaylistEntity.kt
package com.tinhtx.player.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val artworkUri: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val trackCount: Int,
    val duration: Long,
    val isUserCreated: Boolean
)
