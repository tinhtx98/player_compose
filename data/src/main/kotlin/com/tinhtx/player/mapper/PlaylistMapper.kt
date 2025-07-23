package com.tinhtx.player.mapper

import com.tinhtx.player.local.database.entities.PlaylistEntity
import com.tinhtx.player.model.PlaylistInfo

object PlaylistMapper {

    fun toEntity(playlistInfo: PlaylistInfo): PlaylistEntity {
        return PlaylistEntity(
            id = playlistInfo.id,
            name = playlistInfo.name,
            description = playlistInfo.description,
            artworkUri = playlistInfo.artworkUri,
            trackCount = playlistInfo.trackCount,
            duration = playlistInfo.duration,
            dateCreated = playlistInfo.dateCreated,
            dateModified = playlistInfo.dateModified
        )
    }

    fun toDomain(entity: PlaylistEntity): PlaylistInfo {
        return PlaylistInfo(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            artworkUri = entity.artworkUri,
            trackCount = entity.trackCount,
            duration = entity.duration,
            dateCreated = entity.dateCreated,
            dateModified = entity.dateModified
        )
    }

    fun toEntityList(playlists: List<PlaylistInfo>): List<PlaylistEntity> {
        return playlists.map { toEntity(it) }
    }

    fun toDomainList(entities: List<PlaylistEntity>): List<PlaylistInfo> {
        return entities.map { toDomain(it) }
    }
}
