// data/src/main/kotlin/com/tinhtx/player/local/database/dao/MediaDao.kt
package com.tinhtx.player.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy
import com.tinhtx.player.data.local.database.entities.MediaEntity
import com.tinhtx.player.domain.model.MediaType
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {

    @Query("SELECT * FROM media_items WHERE isAvailable = 1")
    fun getAllMediaItems(): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media_items WHERE type = :type AND isAvailable = 1")
    fun getMediaItemsByType(type: MediaType): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media_items WHERE id = :id")
    fun getMediaItemById(id: String): Flow<MediaEntity?>

    @Query("SELECT * FROM media_items WHERE id IN (:ids)")
    fun getMediaItemsByIds(ids: List<String>): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media_items WHERE isFavorite = 1 AND isAvailable = 1")
    fun getFavoriteMediaItems(): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media_items WHERE lastPlayed IS NOT NULL AND isAvailable = 1 ORDER BY lastPlayed DESC LIMIT :limit")
    fun getRecentlyPlayedItems(limit: Int): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media_items WHERE isAvailable = 1 ORDER BY dateAdded DESC LIMIT :limit")
    fun getRecentlyAddedItems(limit: Int): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media_items WHERE isAvailable = 1 ORDER BY playCount DESC LIMIT :limit")
    fun getMostPlayedItems(limit: Int): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media_items WHERE (title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%' OR album LIKE '%' || :query || '%') AND isAvailable = 1")
    fun searchMediaItems(query: String): Flow<List<MediaEntity>>

    @Query("SELECT DISTINCT genre FROM media_items WHERE genre IS NOT NULL AND isAvailable = 1")
    fun getGenres(): Flow<List<String>>

    @Query("SELECT * FROM media_items WHERE genre = :genre AND isAvailable = 1")
    fun getMediaItemsByGenre(genre: String): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media_items WHERE album = :album AND isAvailable = 1")
    fun getMediaItemsByAlbum(album: String): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media_items WHERE artist = :artist AND isAvailable = 1")
    fun getMediaItemsByArtist(artist: String): Flow<List<MediaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaItem(mediaItem: MediaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaItems(mediaItems: List<MediaEntity>)

    @Update
    suspend fun updateMediaItem(mediaItem: MediaEntity)

    @Query("UPDATE media_items SET playCount = playCount + 1 WHERE id = :mediaId")
    suspend fun incrementPlayCount(mediaId: String)

    @Query("UPDATE media_items SET lastPlayed = :timestamp WHERE id = :mediaId")
    suspend fun updateLastPlayed(mediaId: String, timestamp: Long)

    @Query("UPDATE media_items SET isFavorite = :isFavorite WHERE id = :mediaId")
    suspend fun setFavorite(mediaId: String, isFavorite: Boolean)

    @Query("DELETE FROM media_items WHERE id = :mediaId")
    suspend fun deleteMediaItem(mediaId: String)

    @Query("DELETE FROM media_items")
    suspend fun deleteAllMediaItems()
}
