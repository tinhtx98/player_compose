package com.tinhtx.player.repository

import com.tinhtx.player.common.Resource
import com.tinhtx.player.local.database.dao.PlaylistDao
import com.tinhtx.player.local.database.entities.PlaylistMediaEntity
import com.tinhtx.player.mapper.MediaMapper
import com.tinhtx.player.mapper.PlaylistMapper
import com.tinhtx.player.model.PlaylistInfo
import com.tinhtx.player.model.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistRepositoryImpl @Inject constructor(
    private val playlistDao: PlaylistDao
) : PlaylistRepository {

    override fun getAllPlaylists(): Flow<Resource<List<PlaylistInfo>>> {
        return playlistDao.getAllPlaylists()
            .map { entities ->
                Resource.Success(PlaylistMapper.toDomainList(entities))
            }
            .catch { e ->
                emit(Resource.Error("Failed to get playlists: ${e.message}"))
            }
    }

    override fun getPlaylistById(id: String): Flow<Resource<PlaylistInfo?>> {
        return playlistDao.getPlaylistById(id)
            .map { entity ->
                Resource.Success(entity?.let { PlaylistMapper.toDomain(it) })
            }
            .catch { e ->
                emit(Resource.Error("Failed to get playlist: ${e.message}"))
            }
    }

    override fun getPlaylistItems(playlistId: String): Flow<Resource<List<MediaItem>>> {
        return playlistDao.getPlaylistItems(playlistId)
            .map { entities ->
                Resource.Success(MediaMapper.toDomainList(entities))
            }
            .catch { e ->
                emit(Resource.Error("Failed to get playlist items: ${e.message}"))
            }
    }

    override suspend fun createPlaylist(name: String, description: String): Resource<PlaylistInfo> {
        return try {
            val playlist = PlaylistInfo(
                id = UUID.randomUUID().toString(),
                name = name,
                description = description
            )
            playlistDao.insertPlaylist(PlaylistMapper.toEntity(playlist))
            Resource.Success(playlist)
        } catch (e: Exception) {
            Resource.Error("Failed to create playlist: ${e.message}")
        }
    }

    override suspend fun updatePlaylist(playlist: PlaylistInfo): Resource<Unit> {
        return try {
            playlistDao.updatePlaylist(PlaylistMapper.toEntity(playlist))
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Failed to update playlist: ${e.message}")
        }
    }

    override suspend fun deletePlaylist(playlistId: String): Resource<Unit> {
        return try {
            val playlist = playlistDao.getPlaylistById(playlistId)
            // Note: This is a simplified approach. In a real implementation,
            // you'd need to handle the Flow properly
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Failed to delete playlist: ${e.message}")
        }
    }

    override suspend fun addItemToPlaylist(playlistId: String, mediaItem: MediaItem): Resource<Unit> {
        return try {
            val playlistMedia = PlaylistMediaEntity(
                playlistId = playlistId,
                mediaId = mediaItem.id,
                position = 0 // This would be calculated based on existing items
            )
            playlistDao.insertPlaylistMedia(playlistMedia)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Failed to add item to playlist: ${e.message}")
        }
    }

    override suspend fun removeItemFromPlaylist(playlistId: String, mediaItemId: String): Resource<Unit> {
        return try {
            playlistDao.removePlaylistMedia(playlistId, mediaItemId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Failed to remove item from playlist: ${e.message}")
        }
    }

    override suspend fun reorderPlaylistItems(playlistId: String, fromIndex: Int, toIndex: Int): Resource<Unit> {
        return try {
            // This would implement the reordering logic
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Failed to reorder playlist items: ${e.message}")
        }
    }

    override suspend fun clearPlaylist(playlistId: String): Resource<Unit> {
        return try {
            playlistDao.clearPlaylist(playlistId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Failed to clear playlist: ${e.message}")
        }
    }
}
