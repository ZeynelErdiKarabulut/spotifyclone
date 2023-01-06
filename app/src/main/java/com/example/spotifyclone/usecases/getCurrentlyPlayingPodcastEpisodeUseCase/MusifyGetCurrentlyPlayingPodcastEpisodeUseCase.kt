package com.example.spotifyclone.usecases.getCurrentlyPlayingPodcastEpisodeUseCase

import com.example.spotifyclone.domain.PodcastEpisode
import com.example.spotifyclone.musicplayer.MusicPlayerV2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class spotifycloneGetCurrentlyPlayingPodcastEpisodeUseCase @Inject constructor(
    private val musicPlayerV2: MusicPlayerV2
) : GetCurrentlyPlayingPodcastEpisodeUseCase {

    override fun getCurrentlyPlayingPodcastEpisodeStream(): Flow<PodcastEpisode?> =
        musicPlayerV2
            .currentPlaybackStateStream
            .filterIsInstance<MusicPlayerV2.PlaybackState.Playing>()
            .filter { it.currentlyPlayingStreamable is PodcastEpisode }
            .map { it.currentlyPlayingStreamable as PodcastEpisode }
}