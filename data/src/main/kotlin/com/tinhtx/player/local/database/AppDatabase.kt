package com.tinhtx.player.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.tinhtx.player.local.database.entities.MediaEntity
import com.tinhtx.player.local.database.entities.PlaylistEntity
import com.tinhtx.player.local.database.entities.PlaylistMediaEntity
import com.tinhtx.player.local.database.dao.MediaDao
import com.tinhtx.player.local.database.dao.PlaylistDao

@Database(
    entities = [
        MediaEntity::class,
        PlaylistEntity::class,
        PlaylistMediaEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun mediaDao(): MediaDao
    abstract fun playlistDao(): PlaylistDao

    companion object {
        const val DATABASE_NAME = "tinhtx_player_database"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
