package com.example.spotifyclone.di

import com.example.spotifyclone.musicplayer.MusicPlayerV2
import com.example.spotifyclone.musicplayer.spotifycloneBackgroundMusicPlayerV2
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Note: The dependencies are not scoped because the underlying
 * media player is always a singleton. [ExoPlayerModule.provideExoplayer]
 * is annotated with @Singleton, therefore any class that depends on it
 * need not be a singleton since the class will be provided the same
 * instance of ExoPlayer.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class MusicPlayerModule {
    @Binds
    @Singleton
    abstract fun bindMusicPlayerV2(
        spotifycloneBackgroundMusicPlayerV2: spotifycloneBackgroundMusicPlayerV2
    ): MusicPlayerV2
}