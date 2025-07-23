package com.tinhtx.player.usecase

import com.tinhtx.player.common.Resource
import com.tinhtx.player.model.PlaylistInfo
import com.tinhtx.player.model.MediaItem
import com.tinhtx.player.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManagePlaylistUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {

    fun getAllPlaylists(): Flow<Resource<List<PlaylistInfo>>> {
        return playlistRepository.getAllPlaylists()
    }

    fun getPlaylistById(id: String): Flow<Resource<PlaylistInfo?>> {
        return playlistRepository.getPlaylistById(id)
    }

    fun getPlaylistItems(playlistId: String): Flow<Resource<List<MediaItem>>> {
        return playlistRepository.getPlaylistItems(playlistId)
    }

    suspend fun createPlaylist(name: String, description: String = ""): Resource<PlaylistInfo> {
        return playlistRepository.createPlaylist(name, description)
    }

    suspend fun updatePlaylist(playlist: PlaylistInfo): Resource<Unit> {
        return playlistRepository.updatePlaylist(playlist)
    }

    suspend fun deletePlaylist(playlistId: String): Resource<Unit> {
        return playlistRepository.deletePlaylist(playlistId)
    }

    suspend fun addItemToPlaylist(playlistId: String, mediaItem: MediaItem): Resource<Unit> {
        return playlistRepository.addItemToPlaylist(playlistId, mediaItem)
    }

    suspend fun removeItemFromPlaylist(playlistId: String, mediaItemId: String): Resource<Unit> {
        return playlistRepository.removeItemFromPlaylist(playlistId, mediaItemId)
    }
}
