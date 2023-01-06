package com.example.spotifyclone.viewmodels

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.spotifyclone.data.repositories.podcastsrepository.PodcastsRepository
import com.example.spotifyclone.data.utils.FetchedResource
import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.domain.PodcastEpisode
import com.example.spotifyclone.musicplayer.MusicPlayerV2
import com.example.spotifyclone.ui.navigation.spotifycloneNavigationDestinations
import com.example.spotifyclone.usecases.getCurrentlyPlayingPodcastEpisodeUseCase.GetCurrentlyPlayingPodcastEpisodeUseCase
import com.example.spotifyclone.usecases.getPlaybackLoadingStatusUseCase.GetPlaybackLoadingStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodcastDetailViewModel @Inject constructor(
    application: Application,
    private val podcastsRepository: PodcastsRepository,
    private val savedStateHandle: SavedStateHandle,
    private val musicPlayerV2: MusicPlayerV2,
    getCurrentlyPlayingPodcastEpisodeUseCase: GetCurrentlyPlayingPodcastEpisodeUseCase,
    getPlaybackLoadingStatusUseCase: GetPlaybackLoadingStatusUseCase
) : AndroidViewModel(application) {

    enum class UiSate { IDLE, LOADING, PLAYBACK_LOADING, ERROR }

    private val _uiState = mutableStateOf(UiSate.IDLE)
    val uiState = _uiState as State<UiSate>
    private val _podcastEpisode = mutableStateOf<PodcastEpisode?>(null)
    val podcastEpisode = _podcastEpisode as State<PodcastEpisode?>

    private var isMusicPlayerPlaying by mutableStateOf<Boolean?>(null)
    private var currentlyPlayingPodcastEpisode by mutableStateOf<PodcastEpisode?>(null)

    val isEpisodeCurrentlyPlaying = derivedStateOf {
        isMusicPlayerPlaying == true && currentlyPlayingPodcastEpisode == podcastEpisode.value
    }

    init {
        fetchEpisodeUpdatingUiState()
        
        getPlaybackLoadingStatusUseCase
            .loadingStatusStream
            .onEach { isPlaybackLoading ->
                if (isPlaybackLoading && _uiState.value != UiSate.PLAYBACK_LOADING) {
                    _uiState.value = UiSate.PLAYBACK_LOADING
                    return@onEach
                }
                if (!isPlaybackLoading && _uiState.value == UiSate.PLAYBACK_LOADING) {
                    _uiState.value = UiSate.IDLE
                    return@onEach
                }
            }
            .launchIn(viewModelScope)

        getCurrentlyPlayingPodcastEpisodeUseCase
            .getCurrentlyPlayingPodcastEpisodeStream()
            .onEach { currentlyPlayingPodcastEpisode = it }
            .launchIn(viewModelScope)
        musicPlayerV2.currentPlaybackStateStream
            .onEach {
                if (it is MusicPlayerV2.PlaybackState.Playing && (isMusicPlayerPlaying == false || isMusicPlayerPlaying == null)) {
                    isMusicPlayerPlaying = true
                    return@onEach
                }
                if (it is MusicPlayerV2.PlaybackState.Ended || it is MusicPlayerV2.PlaybackState.Paused) {
                    if (isMusicPlayerPlaying == true) isMusicPlayerPlaying = false
                }
            }
            .launchIn(viewModelScope)
    }

    private fun fetchEpisodeUpdatingUiState() {
        viewModelScope.launch {
            _uiState.value = UiSate.LOADING
            val episode = fetchEpisode()
            _uiState.value = if (episode == null) {
                UiSate.ERROR
            } else {
                _podcastEpisode.value = episode
                UiSate.IDLE
            }
        }
    }

    private suspend fun fetchEpisode(): PodcastEpisode? {
        val fetchedResource = podcastsRepository.fetchPodcastEpisode(
            episodeId = savedStateHandle[spotifycloneNavigationDestinations.PodcastEpisodeDetailScreen.NAV_ARG_PODCAST_EPISODE_ID]!!,
            countryCode = getCountryCode(),
            imageSize = MapperImageSize.LARGE // image would be used for both the mini player and the full screen player
        )
        return if (fetchedResource is FetchedResource.Success) fetchedResource.data else null
    }

    fun retryFetchingEpisode() {
        fetchEpisodeUpdatingUiState()
    }
}