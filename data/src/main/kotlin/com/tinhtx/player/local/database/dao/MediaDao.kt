package com.tinhtx.player.local.database.dao

import androidx.room.*
import com.tinhtx.player.local.database.entities.MediaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {

    @Query("SELECT * FROM media_items")
    fun getAllMediaItems(): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media_items WHERE mediaType = 'AUDIO'")
    fun getAudioItems(): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media_items WHERE mediaType = 'VIDEO'")
    fun getVideoItems(): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media_items WHERE id = :id")
    fun getMediaItemById(id: String): Flow<MediaEntity?>

    @Query("SELECT * FROM media_items WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%' OR album LIKE '%' || :query || '%'")
    fun searchMediaItems(query: String): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media_items WHERE album = :albumId")
    fun getMediaItemsByAlbum(albumId: String): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media_items WHERE artist = :artistId")
    fun getMediaItemsByArtist(artistId: String): Flow<List<MediaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaItem(mediaItem: MediaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaItems(mediaItems: List<MediaEntity>)

    @Update
    suspend fun updateMediaItem(mediaItem: MediaEntity)

    @Delete
    suspend fun deleteMediaItem(mediaItem: MediaEntity)

    @Query("DELETE FROM media_items")
    suspend fun deleteAllMediaItems()
}
