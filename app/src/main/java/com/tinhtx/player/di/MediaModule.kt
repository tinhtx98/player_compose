package com.tinhtx.player.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.tinhtx.player.equalizer.EqualizerManager
import com.tinhtx.player.playback.ExoPlayerManager
import com.tinhtx.player.playback.MediaNotificationManager
import com.tinhtx.player.pip.PictureInPictureManager
import com.tinhtx.player.queue.PlaybackQueue
import com.tinhtx.player.scanner.MediaStoreScanner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {

    @Provides
    @Singleton
    fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer {
        return ExoPlayer.Builder(context).build()
    }

    @Provides
    @Singleton
    fun provideExoPlayerManager(@ApplicationContext context: Context): ExoPlayerManager {
        return ExoPlayerManager(context)
    }

    @Provides
    @Singleton
    fun provideMediaNotificationManager(@ApplicationContext context: Context): MediaNotificationManager {
        return MediaNotificationManager(context)
    }

    @Provides
    @Singleton
    fun provideMediaStoreScanner(@ApplicationContext context: Context): MediaStoreScanner {
        return MediaStoreScanner(context)
    }

    @Provides
    @Singleton
    fun providePlaybackQueue(): PlaybackQueue {
        return PlaybackQueue()
    }

    @Provides
    @Singleton
    fun provideEqualizerManager(): EqualizerManager {
        return EqualizerManager()
    }

    @Provides
    @Singleton
    fun providePictureInPictureManager(@ApplicationContext context: Context): PictureInPictureManager {
        return PictureInPictureManager(context)
    }
}