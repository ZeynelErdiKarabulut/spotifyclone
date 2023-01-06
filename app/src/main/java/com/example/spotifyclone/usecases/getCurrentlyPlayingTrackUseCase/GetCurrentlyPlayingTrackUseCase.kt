package com.example.spotifyclone.usecases.getCurrentlyPlayingTrackUseCase

import com.example.spotifyclone.domain.SearchResult
import kotlinx.coroutines.flow.Flow

interface GetCurrentlyPlayingTrackUseCase {
    fun getCurrentlyPlayingTrackStream(): Flow<SearchResult.TrackSearchResult?>
}