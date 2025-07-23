package com.tinhtx.player.local.database.dao

import androidx.room.*
import com.tinhtx.player.local.database.entities.PlaylistEntity
import com.tinhtx.player.local.database.entities.PlaylistMediaEntity
import com.tinhtx.player.local.database.entities.MediaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM playlists")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id = :id")
    fun getPlaylistById(id: String): Flow<PlaylistEntity?>

    @Query("""
        SELECT m.* FROM media_items m
        INNER JOIN playlist_media pm ON m.id = pm.mediaId
        WHERE pm.playlistId = :playlistId
        ORDER BY pm.position
    """)
    fun getPlaylistItems(playlistId: String): Flow<List<MediaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistMedia(playlistMedia: PlaylistMediaEntity)

    @Query("DELETE FROM playlist_media WHERE playlistId = :playlistId AND mediaId = :mediaId")
    suspend fun removePlaylistMedia(playlistId: String, mediaId: String)

    @Query("DELETE FROM playlist_media WHERE playlistId = :playlistId")
    suspend fun clearPlaylist(playlistId: String)

    @Query("UPDATE playlist_media SET position = :newPosition WHERE playlistId = :playlistId AND mediaId = :mediaId")
    suspend fun updateMediaPosition(playlistId: String, mediaId: String, newPosition: Int)
}
