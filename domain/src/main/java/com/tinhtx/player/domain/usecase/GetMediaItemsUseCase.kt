// domain/src/main/kotlin/com/tinhtx/player/usecase/GetMediaItemsUseCase.kt
package com.tinhtx.player.domain.usecase

import com.tinhtx.player.domain.model.AlbumInfo
import com.tinhtx.player.domain.model.ArtistInfo
import com.tinhtx.player.domain.model.MediaItem
import com.tinhtx.player.domain.model.MediaType
import com.tinhtx.player.domain.model.SortBy
import com.tinhtx.player.domain.model.SortOrder
import com.tinhtx.player.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMediaItemsUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    fun getAllMediaItems(): Flow<List<MediaItem>> = mediaRepository.getAllMediaItems()
    fun getMediaItemsByType(type: MediaType): Flow<List<MediaItem>> = mediaRepository.getMediaItemsByType(type)
    fun getMediaItemById(id: String): Flow<MediaItem?> = mediaRepository.getMediaItemById(id)
    fun getFavoriteItems(): Flow<List<MediaItem>> = mediaRepository.getFavoriteMediaItems()
    fun getRecentlyPlayed(): Flow<List<MediaItem>> = mediaRepository.getRecentlyPlayedItems(20)
    fun getRecentlyAdded(): Flow<List<MediaItem>> = mediaRepository.getRecentlyAddedItems(20)
    fun getMostPlayed(): Flow<List<MediaItem>> = mediaRepository.getMostPlayedItems(20)
    fun searchMediaItems(query: String): Flow<List<MediaItem>> = mediaRepository.searchMediaItems(query)
    fun getAlbums(): Flow<List<AlbumInfo>> = mediaRepository.getAlbums()
    fun getArtists(): Flow<List<ArtistInfo>> = mediaRepository.getArtists()
    fun getGenres(): Flow<List<String>> = mediaRepository.getGenres()
    fun getSortedMediaItems(sortBy: SortBy, sortOrder: SortOrder, type: MediaType?): Flow<List<MediaItem>> =
        mediaRepository.getSortedMediaItems(sortBy, sortOrder, type)
}
