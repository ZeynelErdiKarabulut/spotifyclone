package com.example.spotifyclone.viewmodels.artistviewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.spotifyclone.data.repositories.albumsrepository.AlbumsRepository
import com.example.spotifyclone.data.repositories.tracksrepository.TracksRepository
import com.example.spotifyclone.data.utils.FetchedResource
import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.domain.SearchResult
import com.example.spotifyclone.ui.navigation.spotifycloneNavigationDestinations
import com.example.spotifyclone.usecases.getCurrentlyPlayingTrackUseCase.GetCurrentlyPlayingTrackUseCase
import com.example.spotifyclone.usecases.getPlaybackLoadingStatusUseCase.GetPlaybackLoadingStatusUseCase
import com.example.spotifyclone.viewmodels.getCountryCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A sealed class hierarchy consisting of all UI states that are related to a screen
 * displaying the details of an artist.
 */
sealed class ArtistDetailScreenUiState {
    object Idle : ArtistDetailScreenUiState()
    object Loading : ArtistDetailScreenUiState()
    data class Error(private val message: String) : ArtistDetailScreenUiState()
}

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    albumsRepository: AlbumsRepository,
    getCurrentlyPlayingTrackUseCase: GetCurrentlyPlayingTrackUseCase,
    getPlaybackLoadingStatusUseCase: GetPlaybackLoadingStatusUseCase,
    private val tracksRepository: TracksRepository,
) : AndroidViewModel(application) {

    private val _popularTracks = mutableStateOf<List<SearchResult.TrackSearchResult>>(emptyList())
    val popularTracks = _popularTracks as State<List<SearchResult.TrackSearchResult>>

    private val _uiState = mutableStateOf<ArtistDetailScreenUiState>(ArtistDetailScreenUiState.Idle)
    val uiState = _uiState as State<ArtistDetailScreenUiState>

    private val defaultMapperImageSize = MapperImageSize.MEDIUM
    private val artistId =
        savedStateHandle.get<String>(spotifycloneNavigationDestinations.ArtistDetailScreen.NAV_ARG_ARTIST_ID)!!

    val currentlyPlayingTrackStream =
        getCurrentlyPlayingTrackUseCase.getCurrentlyPlayingTrackStream()

    val albumsOfArtistFlow = albumsRepository.getPaginatedStreamForAlbumsOfArtist(
        artistId = artistId, countryCode = getCountryCode(), imageSize = defaultMapperImageSize
    ).cachedIn(viewModelScope)

    init {
        viewModelScope.launch { fetchAndAssignPopularTracks() }
        getPlaybackLoadingStatusUseCase.loadingStatusStream.onEach { isPlaybackLoading ->
            if (isPlaybackLoading && _uiState.value !is ArtistDetailScreenUiState.Loading) {
                _uiState.value = ArtistDetailScreenUiState.Loading
                return@onEach
            }
            if (!isPlaybackLoading && _uiState.value is ArtistDetailScreenUiState.Loading) {
                _uiState.value = ArtistDetailScreenUiState.Idle
                return@onEach
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun fetchAndAssignPopularTracks() {
        _uiState.value = ArtistDetailScreenUiState.Loading
        val fetchResult = tracksRepository.fetchTopTenTracksForArtistWithId(
            artistId = artistId,
            imageSize = defaultMapperImageSize,
            countryCode = getCountryCode()
        )
        when (fetchResult) {
            is FetchedResource.Failure -> {
                _uiState.value =
                    ArtistDetailScreenUiState.Error("Error loading tracks, please check internet connection")
            }
            is FetchedResource.Success -> {
                _popularTracks.value = fetchResult.data
                _uiState.value = ArtistDetailScreenUiState.Idle
            }
        }
    }
}