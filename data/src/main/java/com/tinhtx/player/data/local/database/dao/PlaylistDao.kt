// data/src/main/kotlin/com/tinhtx/player/local/database/dao/PlaylistDao.kt
package com.tinhtx.player.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import com.tinhtx.player.data.local.database.entities.PlaylistEntity
import com.tinhtx.player.data.local.database.entities.PlaylistMediaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM playlists")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id = :id")
    fun getPlaylistById(id: String): Flow<PlaylistEntity?>

    @Query("SELECT * FROM playlist_media WHERE playlistId = :playlistId ORDER BY position")
    fun getPlaylistMediaItems(playlistId: String): Flow<List<PlaylistMediaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistMedia(playlistMedia: PlaylistMediaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistMediaItems(playlistMediaItems: List<PlaylistMediaEntity>)

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("DELETE FROM playlist_media WHERE playlistId = :playlistId")
    suspend fun deletePlaylistMediaItems(playlistId: String)

    @Query("DELETE FROM playlist_media WHERE playlistId = :playlistId AND mediaId = :mediaId")
    suspend fun deletePlaylistMediaItem(playlistId: String, mediaId: String)
}
