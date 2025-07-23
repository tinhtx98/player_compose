package com.tinhtx.player.repository

import com.tinhtx.player.common.Resource
import com.tinhtx.player.model.MediaItem
import com.tinhtx.player.model.AlbumInfo
import com.tinhtx.player.model.ArtistInfo
import com.tinhtx.player.model.SortOrder
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    fun getAllMediaItems(): Flow<Resource<List<MediaItem>>>

    fun getAudioItems(): Flow<Resource<List<MediaItem>>>

    fun getVideoItems(): Flow<Resource<List<MediaItem>>>

    fun getMediaItemById(id: String): Flow<Resource<MediaItem?>>

    fun getAlbums(): Flow<Resource<List<AlbumInfo>>>

    fun getArtists(): Flow<Resource<List<ArtistInfo>>>

    fun getMediaItemsByAlbum(albumId: String): Flow<Resource<List<MediaItem>>>

    fun getMediaItemsByArtist(artistId: String): Flow<Resource<List<MediaItem>>>

    fun searchMediaItems(query: String): Flow<Resource<List<MediaItem>>>

    fun sortMediaItems(items: List<MediaItem>, sortOrder: SortOrder): List<MediaItem>

    suspend fun scanMediaFiles(): Resource<Unit>

    suspend fun refreshMediaLibrary(): Resource<Unit>
}
