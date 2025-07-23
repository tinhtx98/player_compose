package com.tinhtx.player.mapper

import com.tinhtx.player.local.database.entities.MediaEntity
import com.tinhtx.player.model.MediaItem
import com.tinhtx.player.model.MediaType

object MediaMapper {

    fun toEntity(mediaItem: MediaItem): MediaEntity {
        return MediaEntity(
            id = mediaItem.id,
            title = mediaItem.title,
            artist = mediaItem.artist,
            album = mediaItem.album,
            duration = mediaItem.duration,
            uri = mediaItem.uri,
            artworkUri = mediaItem.artworkUri,
            mediaType = mediaItem.mediaType.name,
            size = mediaItem.size,
            dateAdded = mediaItem.dateAdded,
            path = mediaItem.path,
            mimeType = mediaItem.mimeType
        )
    }

    fun toDomain(entity: MediaEntity): MediaItem {
        return MediaItem(
            id = entity.id,
            title = entity.title,
            artist = entity.artist,
            album = entity.album,
            duration = entity.duration,
            uri = entity.uri,
            artworkUri = entity.artworkUri,
            mediaType = MediaType.valueOf(entity.mediaType),
            size = entity.size,
            dateAdded = entity.dateAdded,
            path = entity.path,
            mimeType = entity.mimeType
        )
    }

    fun toEntityList(mediaItems: List<MediaItem>): List<MediaEntity> {
        return mediaItems.map { toEntity(it) }
    }

    fun toDomainList(entities: List<MediaEntity>): List<MediaItem> {
        return entities.map { toDomain(it) }
    }
}
