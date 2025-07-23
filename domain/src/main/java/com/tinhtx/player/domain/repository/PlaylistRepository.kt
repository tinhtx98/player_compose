// domain/src/main/kotlin/com/tinhtx/player/repository/PlaylistRepository.kt
package com.tinhtx.player.domain.repository

import com.tinhtx.player.domain.model.PlaylistInfo
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getAllPlaylists(): Flow<List<PlaylistInfo>>
    fun getPlaylistById(id: String): Flow<PlaylistInfo?>
    suspend fun createPlaylist(name: String, description: String?)
    suspend fun updatePlaylist(playlist: PlaylistInfo)
    suspend fun deletePlaylist(playlistId: String)
    suspend fun addMediaToPlaylist(playlistId: String, mediaId: String, position: Int)
    suspend fun removeMediaFromPlaylist(playlistId: String, mediaId: String)
}
