package com.example.spotifyclone.usecases.getCurrentlyPlayingPodcastEpisodeUseCase

import com.example.spotifyclone.domain.PodcastEpisode
import kotlinx.coroutines.flow.Flow

interface GetCurrentlyPlayingPodcastEpisodeUseCase {
    fun getCurrentlyPlayingPodcastEpisodeStream(): Flow<PodcastEpisode?>
}