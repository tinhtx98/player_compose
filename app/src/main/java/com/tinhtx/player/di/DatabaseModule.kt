package com.tinhtx.player.di

import android.content.Context
import androidx.room.Room
import com.tinhtx.player.local.database.AppDatabase
import com.tinhtx.player.local.database.dao.MediaDao
import com.tinhtx.player.local.database.dao.PlaylistDao
import com.tinhtx.player.local.preferences.UserPreferencesManager
import com.tinhtx.player.repository.MediaRepository
import com.tinhtx.player.repository.MediaRepositoryImpl
import com.tinhtx.player.repository.PlaylistRepository
import com.tinhtx.player.repository.PlaylistRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "tinhtx_player_database"
        ).build()
    }

    @Provides
    fun provideMediaDao(database: AppDatabase): MediaDao {
        return database.mediaDao()
    }

    @Provides
    fun providePlaylistDao(database: AppDatabase): PlaylistDao {
        return database.playlistDao()
    }

    @Provides
    @Singleton
    fun provideUserPreferencesManager(@ApplicationContext context: Context): UserPreferencesManager {
        return UserPreferencesManager(context)
    }

    @Provides
    @Singleton
    fun provideMediaRepository(mediaDao: MediaDao): MediaRepository {
        return MediaRepositoryImpl(mediaDao)
    }

    @Provides
    @Singleton
    fun providePlaylistRepository(playlistDao: PlaylistDao): PlaylistRepository {
        return PlaylistRepositoryImpl(playlistDao)
    }
}