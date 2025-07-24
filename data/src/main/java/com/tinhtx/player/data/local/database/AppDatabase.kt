// data/src/main/kotlin/com/tinhtx/player/local/database/AppDatabase.kt
package com.tinhtx.player.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.tinhtx.player.data.local.database.dao.MediaDao
import com.tinhtx.player.data.local.database.dao.PlaylistDao
import com.tinhtx.player.data.local.database.entities.MediaEntity
import com.tinhtx.player.data.local.database.entities.PlaylistEntity
import com.tinhtx.player.data.local.database.entities.PlaylistMediaEntity
import com.tinhtx.player.domain.model.MediaType

@Database(
    entities = [
        MediaEntity::class,
        PlaylistEntity::class,
        PlaylistMediaEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun mediaDao(): MediaDao
    abstract fun playlistDao(): PlaylistDao
}

class Converters {
    @TypeConverter
    fun fromMediaType(mediaType: MediaType): String {
        return mediaType.name
    }

    @TypeConverter
    fun toMediaType(mediaType: String): MediaType {
        return MediaType.valueOf(mediaType)
    }
}
