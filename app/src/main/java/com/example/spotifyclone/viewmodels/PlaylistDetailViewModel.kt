package com.example.spotifyclone.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.spotifyclone.data.repositories.tracksrepository.TracksRepository
import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.ui.navigation.spotifycloneNavigationDestinations
import com.example.spotifyclone.usecases.getCurrentlyPlayingTrackUseCase.GetCurrentlyPlayingTrackUseCase
import com.example.spotifyclone.usecases.getPlaybackLoadingStatusUseCase.GetPlaybackLoadingStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    tracksRepository: TracksRepository,
    getCurrentlyPlayingTrackUseCase: GetCurrentlyPlayingTrackUseCase,
    getPlaybackLoadingStatusUseCase: GetPlaybackLoadingStatusUseCase,
) : AndroidViewModel(application) {
    private val playlistId =
        savedStateHandle.get<String>(spotifycloneNavigationDestinations.PlaylistDetailScreen.NAV_ARG_PLAYLIST_ID)!!
    val playbackLoadingStateStream = getPlaybackLoadingStatusUseCase.loadingStatusStream
    val currentlyPlayingTrackStream =
        getCurrentlyPlayingTrackUseCase.getCurrentlyPlayingTrackStream()
    val tracks = tracksRepository.getPaginatedStreamForPlaylistTracks(
        playlistId = playlistId,
        countryCode = getCountryCode(),
        imageSize = MapperImageSize.MEDIUM
    ).cachedIn(viewModelScope)
}