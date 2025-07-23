// domain/src/main/kotlin/com/tinhtx/player/usecase/ManagePlaylistUseCase.kt
package com.tinhtx.player.domain.usecase

import com.tinhtx.player.domain.model.PlaylistInfo
import com.tinhtx.player.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManagePlaylistUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {
    fun getAllPlaylists(): Flow<List<PlaylistInfo>> = playlistRepository.getAllPlaylists()
    fun getPlaylistById(id: String): Flow<PlaylistInfo?> = playlistRepository.getPlaylistById(id)
    suspend fun createPlaylist(name: String, description: String?) = playlistRepository.createPlaylist(name, description)
    suspend fun updatePlaylist(playlist: PlaylistInfo) = playlistRepository.updatePlaylist(playlist)
    suspend fun deletePlaylist(playlistId: String) = playlistRepository.deletePlaylist(playlistId)
    suspend fun addMediaToPlaylist(playlistId: String, mediaId: String, position: Int) =
        playlistRepository.addMediaToPlaylist(playlistId, mediaId, position)
    suspend fun removeMediaFromPlaylist(playlistId: String, mediaId: String) =
        playlistRepository.removeMediaFromPlaylist(playlistId, mediaId)
}
