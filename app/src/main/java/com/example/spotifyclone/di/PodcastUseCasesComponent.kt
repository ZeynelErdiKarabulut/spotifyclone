package com.example.spotifyclone.di

import com.example.spotifyclone.usecases.getCurrentlyPlayingPodcastEpisodeUseCase.GetCurrentlyPlayingPodcastEpisodeUseCase
import com.example.spotifyclone.usecases.getCurrentlyPlayingPodcastEpisodeUseCase.spotifycloneGetCurrentlyPlayingPodcastEpisodeUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class PodcastUseCasesComponent {
    @Binds
    abstract fun bindGetCurrentlyPlayingPodcastEpisodeUseCase(
        impl: spotifycloneGetCurrentlyPlayingPodcastEpisodeUseCase
    ): GetCurrentlyPlayingPodcastEpisodeUseCase
}