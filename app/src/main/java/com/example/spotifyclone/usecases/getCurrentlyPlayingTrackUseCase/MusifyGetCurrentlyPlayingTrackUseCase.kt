package com.example.spotifyclone.usecases.getCurrentlyPlayingTrackUseCase

import com.example.spotifyclone.domain.SearchResult
import com.example.spotifyclone.musicplayer.MusicPlayerV2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class spotifycloneGetCurrentlyPlayingTrackUseCase @Inject constructor(
    val musicPlayer: MusicPlayerV2
) : GetCurrentlyPlayingTrackUseCase {
    override fun getCurrentlyPlayingTrackStream(): Flow<SearchResult.TrackSearchResult> =
        musicPlayer.currentPlaybackStateStream
            .filterIsInstance<MusicPlayerV2.PlaybackState.Playing>()
            .filter { it.currentlyPlayingStreamable is SearchResult.TrackSearchResult }
            .map { it.currentlyPlayingStreamable as SearchResult.TrackSearchResult }
}