// data/src/main/kotlin/com/tinhtx/player/local/database/AppDatabase.kt
package com.tinhtx.player.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tinhtx.player.data.local.database.dao.MediaDao
import com.tinhtx.player.data.local.database.dao.PlaylistDao
import com.tinhtx.player.data.local.database.entities.MediaEntity
import com.tinhtx.player.data.local.database.entities.PlaylistEntity
import com.tinhtx.player.data.local.database.entities.PlaylistMediaEntity

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
    // Add type converters for enum classes if needed
}
