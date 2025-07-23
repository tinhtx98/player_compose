// data/src/main/kotlin/com/tinhtx/player/repository/PlaylistRepositoryImpl.kt
package com.tinhtx.player.data.repository

import com.tinhtx.player.data.local.database.dao.PlaylistDao
import com.tinhtx.player.data.local.database.entities.PlaylistMediaEntity
import com.tinhtx.player.data.mapper.toDomain
import com.tinhtx.player.data.mapper.toEntity
import com.tinhtx.player.di.IoDispatcher
import com.tinhtx.player.domain.model.PlaylistInfo
import com.tinhtx.player.domain.repository.PlaylistRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    private val playlistDao: PlaylistDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : PlaylistRepository {

    override fun getAllPlaylists(): Flow<List<PlaylistInfo>> {
        return playlistDao.getAllPlaylists().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getPlaylistById(id: String): Flow<PlaylistInfo?> {
        return combine(
            playlistDao.getPlaylistById(id),
            playlistDao.getPlaylistMediaItems(id)
        ) { playlistEntity, mediaEntities ->
            playlistEntity?.toDomain()?.copy(
                // In a real implementation, you would fetch associated media items
                // and map them to domain model
            )
        }
    }

    override suspend fun createPlaylist(name: String, description: String?) {
        withContext(dispatcher) {
            val currentTime = System.currentTimeMillis()
            val playlist = PlaylistInfo(
                id = generateUniqueId(),
                name = name,
                description = description,
                artworkUri = null,
                createdAt = currentTime,
                updatedAt = currentTime,
                trackCount = 0,
                duration = 0L,
                isUserCreated = true,
                tracks = emptyList()
            )
            playlistDao.insertPlaylist(playlist.toEntity())
        }
    }

    override suspend fun updatePlaylist(playlist: PlaylistInfo) {
        withContext(dispatcher) {
            playlistDao.updatePlaylist(playlist.toEntity())
        }
    }

    override suspend fun deletePlaylist(playlistId: String) {
        withContext(dispatcher) {
            playlistDao.getPlaylistById(playlistId).collect { playlistEntity ->
                if (playlistEntity != null) {
                    playlistDao.deletePlaylist(playlistEntity)
                    playlistDao.deletePlaylistMediaItems(playlistId)
                }
            }
        }
    }

    override suspend fun addMediaToPlaylist(playlistId: String, mediaId: String, position: Int) {
        withContext(dispatcher) {
            val playlistMedia = PlaylistMediaEntity(
                playlistId = playlistId,
                mediaId = mediaId,
                position = position,
                addedAt = System.currentTimeMillis()
            )
            playlistDao.insertPlaylistMedia(playlistMedia)
        }
    }

    override suspend fun removeMediaFromPlaylist(playlistId: String, mediaId: String) {
        withContext(dispatcher) {
            playlistDao.deletePlaylistMediaItem(playlistId, mediaId)
        }
    }

    private fun generateUniqueId(): String {
        return System.currentTimeMillis().toString() + "_" + (0..9999).random().toString()
    }
}
