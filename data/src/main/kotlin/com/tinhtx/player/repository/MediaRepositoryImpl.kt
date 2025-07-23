package com.tinhtx.player.repository

import com.tinhtx.player.common.Resource
import com.tinhtx.player.local.database.dao.MediaDao
import com.tinhtx.player.mapper.MediaMapper
import com.tinhtx.player.model.MediaItem
import com.tinhtx.player.model.AlbumInfo
import com.tinhtx.player.model.ArtistInfo
import com.tinhtx.player.model.SortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaRepositoryImpl @Inject constructor(
    private val mediaDao: MediaDao
) : MediaRepository {

    override fun getAllMediaItems(): Flow<Resource<List<MediaItem>>> {
        return mediaDao.getAllMediaItems()
            .map { entities ->
                Resource.Success(MediaMapper.toDomainList(entities))
            }
            .catch { e ->
                emit(Resource.Error("Failed to get media items: ${e.message}"))
            }
    }

    override fun getAudioItems(): Flow<Resource<List<MediaItem>>> {
        return mediaDao.getAudioItems()
            .map { entities ->
                Resource.Success(MediaMapper.toDomainList(entities))
            }
            .catch { e ->
                emit(Resource.Error("Failed to get audio items: ${e.message}"))
            }
    }

    override fun getVideoItems(): Flow<Resource<List<MediaItem>>> {
        return mediaDao.getVideoItems()
            .map { entities ->
                Resource.Success(MediaMapper.toDomainList(entities))
            }
            .catch { e ->
                emit(Resource.Error("Failed to get video items: ${e.message}"))
            }
    }

    override fun getMediaItemById(id: String): Flow<Resource<MediaItem?>> {
        return mediaDao.getMediaItemById(id)
            .map { entity ->
                Resource.Success(entity?.let { MediaMapper.toDomain(it) })
            }
            .catch { e ->
                emit(Resource.Error("Failed to get media item: ${e.message}"))
            }
    }

    override fun getAlbums(): Flow<Resource<List<AlbumInfo>>> {
        // This would be implemented with proper album aggregation logic
        return getAllMediaItems().map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val albums = resource.data?.groupBy { it.album }?.map { (albumName, items) ->
                        AlbumInfo(
                            id = items.first().album,
                            name = albumName,
                            artist = items.first().artist,
                            artworkUri = items.first().artworkUri,
                            trackCount = items.size,
                            duration = items.sumOf { it.duration }
                        )
                    } ?: emptyList()
                    Resource.Success(albums)
                }
                is Resource.Error -> Resource.Error(resource.message ?: "Unknown error")
                is Resource.Loading -> Resource.Loading()
            }
        }
    }

    override fun getArtists(): Flow<Resource<List<ArtistInfo>>> {
        return getAllMediaItems().map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val artists = resource.data?.groupBy { it.artist }?.map { (artistName, items) ->
                        ArtistInfo(
                            id = artistName,
                            name = artistName,
                            trackCount = items.size,
                            albumCount = items.map { it.album }.distinct().size,
                            artworkUri = items.first().artworkUri
                        )
                    } ?: emptyList()
                    Resource.Success(artists)
                }
                is Resource.Error -> Resource.Error(resource.message ?: "Unknown error")
                is Resource.Loading -> Resource.Loading()
            }
        }
    }

    override fun getMediaItemsByAlbum(albumId: String): Flow<Resource<List<MediaItem>>> {
        return mediaDao.getMediaItemsByAlbum(albumId)
            .map { entities ->
                Resource.Success(MediaMapper.toDomainList(entities))
            }
            .catch { e ->
                emit(Resource.Error("Failed to get album items: ${e.message}"))
            }
    }

    override fun getMediaItemsByArtist(artistId: String): Flow<Resource<List<MediaItem>>> {
        return mediaDao.getMediaItemsByArtist(artistId)
            .map { entities ->
                Resource.Success(MediaMapper.toDomainList(entities))
            }
            .catch { e ->
                emit(Resource.Error("Failed to get artist items: ${e.message}"))
            }
    }

    override fun searchMediaItems(query: String): Flow<Resource<List<MediaItem>>> {
        return mediaDao.searchMediaItems(query)
            .map { entities ->
                Resource.Success(MediaMapper.toDomainList(entities))
            }
            .catch { e ->
                emit(Resource.Error("Failed to search media items: ${e.message}"))
            }
    }

    override fun sortMediaItems(items: List<MediaItem>, sortOrder: SortOrder): List<MediaItem> {
        return when (sortOrder) {
            SortOrder.TITLE_ASC -> items.sortedBy { it.title }
            SortOrder.TITLE_DESC -> items.sortedByDescending { it.title }
            SortOrder.ARTIST_ASC -> items.sortedBy { it.artist }
            SortOrder.ARTIST_DESC -> items.sortedByDescending { it.artist }
            SortOrder.ALBUM_ASC -> items.sortedBy { it.album }
            SortOrder.ALBUM_DESC -> items.sortedByDescending { it.album }
            SortOrder.DURATION_ASC -> items.sortedBy { it.duration }
            SortOrder.DURATION_DESC -> items.sortedByDescending { it.duration }
            SortOrder.DATE_ADDED_ASC -> items.sortedBy { it.dateAdded }
            SortOrder.DATE_ADDED_DESC -> items.sortedByDescending { it.dateAdded }
        }
    }

    override suspend fun scanMediaFiles(): Resource<Unit> {
        return try {
            // This would be implemented with actual media scanning logic
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Failed to scan media files: ${e.message}")
        }
    }

    override suspend fun refreshMediaLibrary(): Resource<Unit> {
        return try {
            // This would be implemented with actual refresh logic
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Failed to refresh media library: ${e.message}")
        }
    }
}
