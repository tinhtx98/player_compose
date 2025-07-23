// data/src/main/kotlin/com/tinhtx/player/mapper/PlaylistMapper.kt
package com.tinhtx.player.data.mapper

import com.tinhtx.player.data.local.database.entities.PlaylistEntity
import com.tinhtx.player.domain.model.PlaylistInfo

fun PlaylistEntity.toDomain(): PlaylistInfo {
    return PlaylistInfo(
        id = this.id,
        name = this.name,
        description = this.description,
        artworkUri = this.artworkUri,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        trackCount = this.trackCount,
        duration = this.duration,
        isUserCreated = this.isUserCreated,
        tracks = emptyList() // Will be populated separately
    )
}

fun PlaylistInfo.toEntity(): PlaylistEntity {
    return PlaylistEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        artworkUri = this.artworkUri,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        trackCount = this.trackCount,
        duration = this.duration,
        isUserCreated = this.isUserCreated
    )
}
