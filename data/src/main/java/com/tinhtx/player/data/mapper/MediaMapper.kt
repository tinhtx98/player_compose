// data/src/main/kotlin/com/tinhtx/player/mapper/MediaMapper.kt
package com.tinhtx.player.data.mapper

import com.tinhtx.player.data.local.database.entities.MediaEntity
import com.tinhtx.player.domain.model.MediaItem

fun MediaEntity.toDomain(): MediaItem {
    return MediaItem(
        id = this.id,
        uri = this.uri,
        displayName = this.displayName,
        title = this.title,
        artist = this.artist,
        album = this.album,
        duration = this.duration,
        size = this.size,
        dateAdded = this.dateAdded,
        dateModified = this.dateModified,
        mimeType = this.mimeType,
        albumArtUri = this.albumArtUri,
        track = this.track,
        year = this.year,
        genre = this.genre,
        bitrate = this.bitrate,
        sampleRate = this.sampleRate,
        type = this.type,
        path = this.path,
        playCount = this.playCount,
        lastPlayed = this.lastPlayed,
        isFavorite = this.isFavorite,
        isAvailable = this.isAvailable
    )
}

fun MediaItem.toEntity(): MediaEntity {
    return MediaEntity(
        id = this.id,
        uri = this.uri,
        displayName = this.displayName,
        title = this.title,
        artist = this.artist,
        album = this.album,
        duration = this.duration,
        size = this.size,
        dateAdded = this.dateAdded,
        dateModified = this.dateModified,
        mimeType = this.mimeType,
        albumArtUri = this.albumArtUri,
        track = this.track,
        year = this.year,
        genre = this.genre,
        bitrate = this.bitrate,
        sampleRate = this.sampleRate,
        type = this.type,
        path = this.path,
        playCount = this.playCount,
        lastPlayed = this.lastPlayed,
        isFavorite = this.isFavorite,
        isAvailable = this.isAvailable
    )
}
