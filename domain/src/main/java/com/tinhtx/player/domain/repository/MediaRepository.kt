// domain/src/main/kotlin/com/tinhtx/player/repository/MediaRepository.kt
package com.tinhtx.player.domain.repository

import com.tinhtx.player.domain.model.AlbumInfo
import com.tinhtx.player.domain.model.ArtistInfo
import com.tinhtx.player.domain.model.MediaItem
import com.tinhtx.player.domain.model.MediaType
import com.tinhtx.player.domain.model.SortBy
import com.tinhtx.player.domain.model.SortOrder
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getAllMediaItems(): Flow<List<MediaItem>>
    fun getMediaItemsByType(type: MediaType): Flow<List<MediaItem>>
    fun getMediaItemById(id: String): Flow<MediaItem?>
    fun getMediaItemsByIds(ids: List<String>): Flow<List<MediaItem>>
    fun getFavoriteMediaItems(): Flow<List<MediaItem>>
    fun getRecentlyPlayedItems(limit: Int): Flow<List<MediaItem>>
    fun getRecentlyAddedItems(limit: Int): Flow<List<MediaItem>>
    fun getMostPlayedItems(limit: Int): Flow<List<MediaItem>>
    fun searchMediaItems(query: String): Flow<List<MediaItem>>
    fun getAlbums(): Flow<List<AlbumInfo>>
    fun getAlbumById(id: String): Flow<AlbumInfo?>
    fun getArtists(): Flow<List<ArtistInfo>>
    fun getArtistById(id: String): Flow<ArtistInfo?>
    fun getGenres(): Flow<List<String>>
    fun getMediaItemsByGenre(genre: String): Flow<List<MediaItem>>
    fun getMediaItemsByAlbum(albumId: String): Flow<List<MediaItem>>
    fun getMediaItemsByArtist(artistId: String): Flow<List<MediaItem>>
    fun getSortedMediaItems(sortBy: SortBy, sortOrder: SortOrder, type: MediaType?): Flow<List<MediaItem>>
    suspend fun updateMediaItem(mediaItem: MediaItem)
    suspend fun updatePlayCount(mediaId: String)
    suspend fun updateLastPlayed(mediaId: String, timestamp: Long)
    suspend fun setFavorite(mediaId: String, isFavorite: Boolean)
    suspend fun scanMediaLibrary()
    suspend fun deleteMediaItem(mediaId: String)
    suspend fun refreshMediaItem(itemId: Long, type: MediaType): MediaItem?
}
