package com.example.spotifyclone.di

import com.example.spotifyclone.usecases.downloadDrawableFromUrlUseCase.DownloadDrawableFromUrlUseCase
import com.example.spotifyclone.usecases.downloadDrawableFromUrlUseCase.spotifycloneDownloadDrawableFromUrlUseCase
import com.example.spotifyclone.usecases.getCurrentlyPlayingTrackUseCase.GetCurrentlyPlayingTrackUseCase
import com.example.spotifyclone.usecases.getCurrentlyPlayingTrackUseCase.spotifycloneGetCurrentlyPlayingTrackUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class TrackUseCasesComponent {
    @Binds
    abstract fun bindDownloadDrawableFromUrlUseCase(
        impl: spotifycloneDownloadDrawableFromUrlUseCase
    ): DownloadDrawableFromUrlUseCase

    @Binds
    abstract fun bindGetCurrentlyPlayingTrackUseCase(
        impl: spotifycloneGetCurrentlyPlayingTrackUseCase
    ): GetCurrentlyPlayingTrackUseCase
}