// app/src/main/java/com/tinhtx/player/di/MediaModule.kt
package com.tinhtx.player.di

import android.content.Context
import com.tinhtx.player.data.di.IoDispatcher as DataIoDispatcher
import com.tinhtx.player.media.di.IoDispatcher as MediaIoDispatcher
import com.tinhtx.player.data.local.database.dao.MediaDao
import com.tinhtx.player.data.local.database.dao.PlaylistDao
import com.tinhtx.player.data.local.preferences.UserPreferencesManager
import com.tinhtx.player.data.repository.MediaRepositoryImpl
import com.tinhtx.player.data.repository.PlaylistRepositoryImpl
import com.tinhtx.player.domain.repository.MediaRepository
import com.tinhtx.player.domain.repository.PlaylistRepository
import com.tinhtx.player.media.equalizer.EqualizerManager
import com.tinhtx.player.media.playback.ExoPlayerManager
import com.tinhtx.player.media.playback.MediaNotificationManager
import com.tinhtx.player.media.scanner.MediaStoreScanner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {

    @Provides
    @Singleton
    fun provideMediaStoreScanner(
        @ApplicationContext context: Context,
        @MediaIoDispatcher dispatcher: CoroutineDispatcher
    ): MediaStoreScanner {
        return MediaStoreScanner(context, dispatcher)
    }

    @Provides
    @Singleton
    fun provideExoPlayerManager(
        exoPlayer: ExoPlayer,
        mediaSession: MediaSession
    ): ExoPlayerManager {
        return ExoPlayerManager(exoPlayer, mediaSession)
    }

    @Provides
    @Singleton
    fun provideMediaNotificationManager(
        @ApplicationContext context: Context,
        exoPlayer: ExoPlayer
    ): MediaNotificationManager {
        return MediaNotificationManager(context, exoPlayer)
    }

    @Provides
    @Singleton
    fun provideEqualizerManager(
        @ApplicationContext context: Context,
        userPreferencesManager: UserPreferencesManager
    ): EqualizerManager {
        return EqualizerManager(context, userPreferencesManager)
    }

    @Provides
    @Singleton
    fun provideMediaRepository(
        mediaDao: MediaDao,
        mediaStoreScanner: MediaStoreScanner,
        @DataIoDispatcher dispatcher: CoroutineDispatcher
    ): MediaRepository {
        return MediaRepositoryImpl(mediaDao, mediaStoreScanner, dispatcher)
    }

    @Provides
    @Singleton
    fun providePlaylistRepository(
        playlistDao: PlaylistDao,
        @DataIoDispatcher dispatcher: CoroutineDispatcher
    ): PlaylistRepository {
        return PlaylistRepositoryImpl(playlistDao, dispatcher)
    }
}
