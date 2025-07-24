// data/src/main/kotlin/com/tinhtx/player/repository/MediaRepositoryImpl.kt
package com.tinhtx.player.data.repository

import com.tinhtx.player.data.local.database.dao.MediaDao
import com.tinhtx.player.data.mapper.toDomain
import com.tinhtx.player.data.mapper.toEntity
import com.tinhtx.player.data.di.IoDispatcher
import com.tinhtx.player.domain.model.AlbumInfo
import com.tinhtx.player.domain.model.ArtistInfo
import com.tinhtx.player.domain.model.MediaItem
import com.tinhtx.player.domain.model.MediaType
import com.tinhtx.player.domain.model.SortBy
import com.tinhtx.player.domain.model.SortOrder
import com.tinhtx.player.domain.repository.MediaRepository
import com.tinhtx.player.media.scanner.MediaStoreScanner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val mediaDao: MediaDao,
    private val mediaStoreScanner: MediaStoreScanner,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : MediaRepository {

    override fun getAllMediaItems(): Flow<List<MediaItem>> =
        mediaDao.getAllMediaItems().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getMediaItemsByType(type: MediaType): Flow<List<MediaItem>> =
        mediaDao.getMediaItemsByType(type).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getMediaItemById(id: String): Flow<MediaItem?> =
        mediaDao.getMediaItemById(id).map { it?.toDomain() }

    override fun getMediaItemsByIds(ids: List<String>): Flow<List<MediaItem>> =
        mediaDao.getMediaItemsByIds(ids).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getFavoriteMediaItems(): Flow<List<MediaItem>> =
        mediaDao.getFavoriteMediaItems().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getRecentlyPlayedItems(limit: Int): Flow<List<MediaItem>> =
        mediaDao.getRecentlyPlayedItems(limit).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getRecentlyAddedItems(limit: Int): Flow<List<MediaItem>> =
        mediaDao.getRecentlyAddedItems(limit).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getMostPlayedItems(limit: Int): Flow<List<MediaItem>> =
        mediaDao.getMostPlayedItems(limit).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun searchMediaItems(query: String): Flow<List<MediaItem>> =
        mediaDao.searchMediaItems(query).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getAlbums(): Flow<List<AlbumInfo>> =
        mediaDao.getAllMediaItems().map { entities ->
            entities.groupBy { it.album }
                .map { (album, tracks) ->
                    AlbumInfo(
                        id = album.hashCode().toString(),
                        name = album,
                        artist = tracks.first().artist,
                        artworkUri = tracks.first().albumArtUri,
                        trackCount = tracks.size,
                        year = tracks.mapNotNull { it.year }.maxOrNull(),
                        genre = tracks.mapNotNull { it.genre }.firstOrNull(),
                        duration = tracks.sumOf { it.duration },
                        tracks = tracks.map { it.toDomain() }
                    )
                }
        }

    override fun getAlbumById(id: String): Flow<AlbumInfo?> =
        getAlbums().map { albums ->
            albums.find { it.id == id }
        }

    override fun getArtists(): Flow<List<ArtistInfo>> =
        mediaDao.getAllMediaItems().map { entities ->
            entities.groupBy { it.artist }
                .map { (artist, tracks) ->
                    val albums = tracks.groupBy { it.album }
                    ArtistInfo(
                        id = artist.hashCode().toString(),
                        name = artist,
                        albumCount = albums.size,
                        trackCount = tracks.size,
                        albums = albums.map { (album, albumTracks) ->
                            AlbumInfo(
                                id = album.hashCode().toString(),
                                name = album,
                                artist = artist,
                                artworkUri = albumTracks.first().albumArtUri,
                                trackCount = albumTracks.size,
                                year = albumTracks.mapNotNull { it.year }.maxOrNull(),
                                genre = albumTracks.mapNotNull { it.genre }.firstOrNull(),
                                duration = albumTracks.sumOf { it.duration },
                                tracks = albumTracks.map { it.toDomain() }
                            )
                        },
                        topTracks = tracks.sortedByDescending { it.playCount }
                            .take(10)
                            .map { it.toDomain() }
                    )
                }
        }

    override fun getArtistById(id: String): Flow<ArtistInfo?> =
        getArtists().map { artists ->
            artists.find { it.id == id }
        }

    override fun getGenres(): Flow<List<String>> =
        mediaDao.getGenres()

    override fun getMediaItemsByGenre(genre: String): Flow<List<MediaItem>> =
        mediaDao.getMediaItemsByGenre(genre).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getMediaItemsByAlbum(albumId: String): Flow<List<MediaItem>> =
        getAlbumById(albumId).map { album ->
            album?.tracks ?: emptyList()
        }

    override fun getMediaItemsByArtist(artistId: String): Flow<List<MediaItem>> =
        getArtistById(artistId).map { artist ->
            artist?.topTracks ?: emptyList()
        }

    override fun getSortedMediaItems(
        sortBy: SortBy,
        sortOrder: SortOrder,
        type: MediaType?
    ): Flow<List<MediaItem>> {
        val baseFlow = type?.let {
            getMediaItemsByType(it)
        } ?: getAllMediaItems()

        return baseFlow.map { items ->
            val sorted = when (sortBy) {
                SortBy.NAME -> items.sortedBy { it.title }
                SortBy.ARTIST -> items.sortedBy { it.artist }
                SortBy.ALBUM -> items.sortedBy { it.album }
                SortBy.DATE_ADDED -> items.sortedBy { it.dateAdded }
                SortBy.DATE_MODIFIED -> items.sortedBy { it.dateModified }
                SortBy.SIZE -> items.sortedBy { it.size }
                SortBy.DURATION -> items.sortedBy { it.duration }
            }

            when (sortOrder) {
                SortOrder.ASCENDING -> sorted
                SortOrder.DESCENDING -> sorted.reversed()
            }
        }
    }

    override suspend fun updateMediaItem(mediaItem: MediaItem) {
        withContext(dispatcher) {
            mediaDao.updateMediaItem(mediaItem.toEntity())
        }
    }

    override suspend fun updatePlayCount(mediaId: String) {
        withContext(dispatcher) {
            mediaDao.incrementPlayCount(mediaId)
        }
    }

    override suspend fun updateLastPlayed(mediaId: String, timestamp: Long) {
        withContext(dispatcher) {
            mediaDao.updateLastPlayed(mediaId, timestamp)
        }
    }

    override suspend fun setFavorite(mediaId: String, isFavorite: Boolean) {
        withContext(dispatcher) {
            mediaDao.setFavorite(mediaId, isFavorite)
        }
    }

    override suspend fun scanMediaLibrary() {
        withContext(dispatcher) {
            val mediaItems = mediaStoreScanner.scanMediaFiles()
            mediaDao.insertMediaItems(mediaItems.map { it.toEntity() })
        }
    }

    override suspend fun deleteMediaItem(mediaId: String) {
        withContext(dispatcher) {
            mediaDao.deleteMediaItem(mediaId)
        }
    }

    override suspend fun refreshMediaItem(itemId: Long, type: MediaType): MediaItem? {
        val refreshedItem = mediaStoreScanner.refreshMediaItem(itemId, type)
        return refreshedItem
    }
}
