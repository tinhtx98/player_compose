package com.tinhtx.player.repository

import com.tinhtx.player.common.Resource
import com.tinhtx.player.model.PlaylistInfo
import com.tinhtx.player.model.MediaItem
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    fun getAllPlaylists(): Flow<Resource<List<PlaylistInfo>>>

    fun getPlaylistById(id: String): Flow<Resource<PlaylistInfo?>>

    fun getPlaylistItems(playlistId: String): Flow<Resource<List<MediaItem>>>

    suspend fun createPlaylist(name: String, description: String = ""): Resource<PlaylistInfo>

    suspend fun updatePlaylist(playlist: PlaylistInfo): Resource<Unit>

    suspend fun deletePlaylist(playlistId: String): Resource<Unit>

    suspend fun addItemToPlaylist(playlistId: String, mediaItem: MediaItem): Resource<Unit>

    suspend fun removeItemFromPlaylist(playlistId: String, mediaItemId: String): Resource<Unit>

    suspend fun reorderPlaylistItems(playlistId: String, fromIndex: Int, toIndex: Int): Resource<Unit>

    suspend fun clearPlaylist(playlistId: String): Resource<Unit>
}
