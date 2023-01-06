package com.example.spotifyclone.di

import com.example.spotifyclone.usecases.getPlaybackLoadingStatusUseCase.GetPlaybackLoadingStatusUseCase
import com.example.spotifyclone.usecases.getPlaybackLoadingStatusUseCase.spotifycloneGetPlaybackLoadingStatusUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MusicPlayerUseCasesComponent {
    @Binds
    abstract fun bindGetPlaybackLoadingStatusUseCase(
        impl: spotifycloneGetPlaybackLoadingStatusUseCase
    ): GetPlaybackLoadingStatusUseCase
}