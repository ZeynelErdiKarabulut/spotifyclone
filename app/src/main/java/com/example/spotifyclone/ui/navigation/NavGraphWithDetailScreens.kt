package com.example.spotifyclone.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.composable
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.spotifyclone.R
import com.example.spotifyclone.domain.PodcastEpisode
import com.example.spotifyclone.domain.SearchResult
import com.example.spotifyclone.domain.Streamable
import com.example.spotifyclone.ui.components.DefaultspotifycloneErrorMessage
import com.example.spotifyclone.ui.components.DefaultspotifycloneLoadingAnimation
import com.example.spotifyclone.ui.screens.AlbumDetailScreen
import com.example.spotifyclone.ui.screens.ArtistDetailScreen
import com.example.spotifyclone.ui.screens.PlaylistDetailScreen
import com.example.spotifyclone.viewmodels.AlbumDetailUiState
import com.example.spotifyclone.viewmodels.AlbumDetailViewModel
import com.example.spotifyclone.viewmodels.PlaylistDetailViewModel
import com.example.spotifyclone.viewmodels.PodcastDetailViewModel
import com.example.spotifyclone.viewmodels.artistviewmodel.ArtistDetailScreenUiState
import com.example.spotifyclone.viewmodels.artistviewmodel.ArtistDetailViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * A nested navigation graph that consists of detail screens.
 *
 * It uses [prefixedWithRouteOfNavGraphRoute] for the nested destinations.
 * For information on why [prefixedWithRouteOfNavGraphRoute] see
 * docs of [NavGraphWithDetailScreensNestedController] class.
 *
 * @param navGraphRoute the destination's unique route
 * @param navController the nav controller to be associated with the nav graph.
 * @param startDestination the route for the start destination.
 * @param playStreamable lambda to execute when a [Streamable] is to be played.
 * @param builder the builder used to define other composables that belong
 * to this nested graph.
 * @see NavGraphBuilder.artistDetailScreen
 * @see NavGraphBuilder.albumDetailScreen
 * @see NavGraphBuilder.playlistDetailScreen
 */
@ExperimentalMaterialApi
fun NavGraphBuilder.navGraphWithDetailScreens(
    navGraphRoute: String,
    navController: NavHostController,
    playStreamable: (Streamable) -> Unit,
    startDestination: String,
    builder: NavGraphBuilder.(nestedController: NavGraphWithDetailScreensNestedController) -> Unit
) {
    val onBackButtonClicked = {
        navController.popBackStack()
        Unit // Need to specify explicitly inorder to avoid compilation errors
    }
    val nestedController = NavGraphWithDetailScreensNestedController(
        navController = navController,
        associatedNavGraphRoute = navGraphRoute,
        playTrack = playStreamable
    )
    navigation(
        route = navGraphRoute,
        startDestination = startDestination
    ) {
        builder(nestedController)
        artistDetailScreen(
            route = spotifycloneNavigationDestinations
                .ArtistDetailScreen
                .prefixedWithRouteOfNavGraphRoute(navGraphRoute),
            arguments = listOf(
                navArgument(spotifycloneNavigationDestinations.ArtistDetailScreen.NAV_ARG_ENCODED_IMAGE_URL_STRING) {
                    nullable = true
                }
            ),
            onBackButtonClicked = onBackButtonClicked,
            onAlbumClicked = nestedController::navigateToDetailScreen,
            onPlayTrack = playStreamable
        )
        albumDetailScreen(
            route = spotifycloneNavigationDestinations
                .AlbumDetailScreen
                .prefixedWithRouteOfNavGraphRoute(navGraphRoute),
            onBackButtonClicked = onBackButtonClicked,
            onPlayTrack = playStreamable
        )
        playlistDetailScreen(
            route = spotifycloneNavigationDestinations
                .PlaylistDetailScreen
                .prefixedWithRouteOfNavGraphRoute(navGraphRoute),
            onBackButtonClicked = onBackButtonClicked,
            onPlayTrack = playStreamable
        )
        podcastEpisodeDetailScreen(
            route = spotifycloneNavigationDestinations.PodcastEpisodeDetailScreen.prefixedWithRouteOfNavGraphRoute(
                navGraphRoute
            ),
            onBackButtonClicked = onBackButtonClicked,
            onPlayButtonClicked = playStreamable,
            navigateToPodcastDetailScreen = {/*TODO*/ }
        )

    }
}

@ExperimentalMaterialApi
private fun NavGraphBuilder.artistDetailScreen(
    route: String,
    onBackButtonClicked: () -> Unit,
    onPlayTrack: (SearchResult.TrackSearchResult) -> Unit,
    onAlbumClicked: (SearchResult.AlbumSearchResult) -> Unit,
    arguments: List<NamedNavArgument> = emptyList()
) {
    composable(route, arguments) { backStackEntry ->
        val viewModel = hiltViewModel<ArtistDetailViewModel>(backStackEntry)
        val arguments = backStackEntry.arguments!!
        val artistName =
            arguments.getString(spotifycloneNavigationDestinations.ArtistDetailScreen.NAV_ARG_ARTIST_NAME)!!
        val artistImageUrlString =
            arguments.getString(spotifycloneNavigationDestinations.ArtistDetailScreen.NAV_ARG_ENCODED_IMAGE_URL_STRING)
                ?.run { URLDecoder.decode(this, StandardCharsets.UTF_8.toString()) }
        val releases = viewModel.albumsOfArtistFlow.collectAsLazyPagingItems()
        val uiState by viewModel.uiState
        val currentlyPlayingTrack by viewModel.currentlyPlayingTrackStream.collectAsState(initial = null)
        ArtistDetailScreen(
            artistName = artistName,
            artistImageUrlString = artistImageUrlString,
            popularTracks = viewModel.popularTracks.value,
            releases = releases,
            currentlyPlayingTrack = currentlyPlayingTrack,
            onBackButtonClicked = onBackButtonClicked,
            onPlayButtonClicked = { /*TODO*/ },
            onTrackClicked = onPlayTrack,
            onAlbumClicked = onAlbumClicked,
            isLoading = uiState is ArtistDetailScreenUiState.Loading,
            fallbackImageRes = R.drawable.ic_outline_account_circle_24,
            isErrorMessageVisible = uiState is ArtistDetailScreenUiState.Error
        )
    }
}

@ExperimentalMaterialApi
private fun NavGraphBuilder.albumDetailScreen(
    route: String,
    onBackButtonClicked: () -> Unit,
    onPlayTrack: (SearchResult.TrackSearchResult) -> Unit
) {
    composable(route) { backStackEntry ->
        val arguments = backStackEntry.arguments!!
        val viewModel = hiltViewModel<AlbumDetailViewModel>()
        val albumArtUrl =
            arguments.getString(spotifycloneNavigationDestinations.AlbumDetailScreen.NAV_ARG_ENCODED_IMAGE_URL_STRING)!!
        val albumName =
            arguments.getString(spotifycloneNavigationDestinations.AlbumDetailScreen.NAV_ARG_ALBUM_NAME)!!
        val artists =
            arguments.getString(spotifycloneNavigationDestinations.AlbumDetailScreen.NAV_ARG_ARTISTS_STRING)!!
        val yearOfRelease =
            arguments.getString(spotifycloneNavigationDestinations.AlbumDetailScreen.NAV_ARG_YEAR_OF_RELEASE_STRING)!!
        val currentlyPlayingTrack by viewModel.currentlyPlayingTrackStream.collectAsState(initial = null)
        AlbumDetailScreen(
            albumName = albumName,
            artistsString = artists,
            yearOfRelease = yearOfRelease,
            albumArtUrlString = albumArtUrl,
            trackList = viewModel.tracks.value,
            onTrackItemClick = onPlayTrack,
            onBackButtonClicked = onBackButtonClicked,
            isLoading = viewModel.uiState.value is AlbumDetailUiState.Loading,
            isErrorMessageVisible = viewModel.uiState.value is AlbumDetailUiState.Error,
            currentlyPlayingTrack = currentlyPlayingTrack
        )
    }
}

@ExperimentalMaterialApi
private fun NavGraphBuilder.playlistDetailScreen(
    route: String,
    onBackButtonClicked: () -> Unit,
    onPlayTrack: (SearchResult.TrackSearchResult) -> Unit,
    navigationArguments: List<NamedNavArgument> = emptyList()
) {
    composable(route = route, arguments = navigationArguments) {
        val arguments = it.arguments!!
        val viewModel = hiltViewModel<PlaylistDetailViewModel>()
        val tracks = viewModel.tracks.collectAsLazyPagingItems()
        val playlistName =
            arguments.getString(spotifycloneNavigationDestinations.PlaylistDetailScreen.NAV_ARG_PLAYLIST_NAME)!!
        val imageUrlString =
            arguments.getString(spotifycloneNavigationDestinations.PlaylistDetailScreen.NAV_ARG_ENCODED_IMAGE_URL_STRING)!!
        val ownerName =
            arguments.getString(spotifycloneNavigationDestinations.PlaylistDetailScreen.NAV_ARG_OWNER_NAME)!!
        val totalNumberOfTracks =
            arguments.getString(spotifycloneNavigationDestinations.PlaylistDetailScreen.NAV_ARG_NUMBER_OF_TRACKS)!!
        val isErrorMessageVisible by remember {
            derivedStateOf {
                tracks.loadState.refresh is LoadState.Error ||
                        tracks.loadState.append is LoadState.Error ||
                        tracks.loadState.prepend is LoadState.Error

            }
        }
        val currentlyPlayingTrack by viewModel.currentlyPlayingTrackStream.collectAsState(initial = null)
        val isPlaybackLoading by viewModel.playbackLoadingStateStream.collectAsState(initial = false)
        PlaylistDetailScreen(
            playlistName = playlistName,
            playlistImageUrlString = imageUrlString,
            nameOfPlaylistOwner = ownerName,
            totalNumberOfTracks = totalNumberOfTracks,
            imageResToUseWhenImageUrlStringIsNull = R.drawable.ic_outline_account_circle_24, // TODO
            tracks = tracks,
            currentlyPlayingTrack = currentlyPlayingTrack,
            onBackButtonClicked = onBackButtonClicked,
            onTrackClicked = onPlayTrack,
            isLoading = tracks.loadState.refresh is LoadState.Loading || isPlaybackLoading,
            isErrorMessageVisible = isErrorMessageVisible
        )
    }
}

/**
 * A class that acts a controller that is used to navigate within
 * destinations defined in [NavGraphBuilder.navGraphWithDetailScreens].
 *
 * Navigation component doesn't work deterministically when the same
 * nested graph is used more than once in the same graph. Since the
 * same destinations defined in [NavGraphBuilder.navGraphWithDetailScreens] are
 * reused (with the same routes) multiple times within the same graph,
 * navigation component chooses the destination that appears in the first call
 * to [NavGraphBuilder.navGraphWithDetailScreens] when ever the client
 * chooses to navigate to one of the screens defined in
 * [NavGraphBuilder.navGraphWithDetailScreens].
 * Eg:
 * Let's assume that NavGraphBuilder.navGraphWithDetailScreens has an artist
 * and album detail screen.
 * ```
 * NavHost(...){
 *
 *      // (1) contains detail screens
 *      navGraphWithDetailScreens(){
 *         /* Other composable destinations */
 *      }
 *
 *      // (2) contains the same detail screens as (1)
 *      navGraphWithDetailScreens(){
 *         /* Other composable destinations */
 *      }
 * }
 *```
 * When the client wants to navigate to a detail screen (lets take album detail
 * screen for example), then, the navigation component will navigate to the
 * album detail screen defined in (1) and not the detail screen defined in (2)
 * even if the client is navigating from one of the composable destinations defined
 * in the second call since the route strings for the detail screens are the same in
 * both graphs ((1) and (2)). This results in navigating to a destination that has an
 * unexpected parent navGraph. In order to avoid this, the destinations defined
 * in [NavGraphBuilder.navGraphWithDetailScreens] are prefixed with the route
 * of the said navGraph using [prefixedWithRouteOfNavGraphRoute]. The
 * [NavGraphWithDetailScreensNestedController.navigateToDetailScreen]
 * prefixes [associatedNavGraphRoute] before navigating in-order to accommodate
 * for this.
 */
class NavGraphWithDetailScreensNestedController(
    private val navController: NavHostController,
    private val associatedNavGraphRoute: String,
    private val playTrack: (SearchResult.TrackSearchResult) -> Unit
) {
    fun navigateToDetailScreen(searchResult: SearchResult) {
        val route = when (searchResult) {
            is SearchResult.AlbumSearchResult -> spotifycloneNavigationDestinations
                .AlbumDetailScreen
                .buildRoute(searchResult)

            is SearchResult.ArtistSearchResult -> spotifycloneNavigationDestinations
                .ArtistDetailScreen
                .buildRoute(searchResult)

            is SearchResult.PlaylistSearchResult -> spotifycloneNavigationDestinations
                .PlaylistDetailScreen
                .buildRoute(searchResult)

            is SearchResult.TrackSearchResult -> {
                playTrack(searchResult)
                return
            }
            is SearchResult.PodcastSearchResult -> {
                TODO()
            }
            is SearchResult.EpisodeSearchResult -> {
                spotifycloneNavigationDestinations.PodcastEpisodeDetailScreen.buildRoute(searchResult.id)

            }
        }
        navController.navigate(associatedNavGraphRoute + route)
    }
}

private fun NavGraphBuilder.podcastEpisodeDetailScreen(
    route: String,
    onPlayButtonClicked: (PodcastEpisode) -> Unit,
    onBackButtonClicked: () -> Unit,
    navigateToPodcastDetailScreen: () -> Unit
) {
    composable(route = route) {
        val viewModel = hiltViewModel<PodcastDetailViewModel>()
        val uiState by viewModel.uiState
        val isEpisodeCurrentlyPlaying by viewModel.isEpisodeCurrentlyPlaying
        if (viewModel.podcastEpisode.value == null) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState == PodcastDetailViewModel.UiSate.LOADING) {
                    DefaultspotifycloneLoadingAnimation(
                        modifier = Modifier.align(Alignment.Center),
                        isVisible = true
                    )
                }
                if (uiState == PodcastDetailViewModel.UiSate.ERROR) {
                    DefaultspotifycloneErrorMessage(
                        modifier = Modifier.align(Alignment.Center),
                        title = "Oops! Something doesn't look right",
                        subtitle = "Please check the internet connection",
                        onRetryButtonClicked = viewModel::retryFetchingEpisode
                    )
                }
            }
        } else {
            com.example.spotifyclone.ui.screens.PodcastEpisodeDetailScreen(
                podcastEpisode = viewModel.podcastEpisode.value!!,
                isEpisodeCurrentlyPlaying = isEpisodeCurrentlyPlaying,
                isPlaybackLoading = uiState == PodcastDetailViewModel.UiSate.PLAYBACK_LOADING,
                onPlayButtonClicked = {
                    onPlayButtonClicked(viewModel.podcastEpisode.value!!)
                },
                onPauseButtonClicked = {
                    /*TODO*/
                },
                onShareButtonClicked = { /*TODO*/ },
                onAddButtonClicked = { /*TODO*/ },
                onDownloadButtonClicked = { /*TODO*/ },
                onBackButtonClicked = onBackButtonClicked,
                navigateToPodcastDetailScreen = navigateToPodcastDetailScreen
            )
        }
    }
}

/**
 * A utility function that appends the [routeOfNavGraph] to [spotifycloneNavigationDestinations.route]
 * as prefix. See docs of [NavGraphWithDetailScreensNestedController] for more information.
 */
private fun spotifycloneNavigationDestinations.prefixedWithRouteOfNavGraphRoute(routeOfNavGraph: String) =
    routeOfNavGraph + this.route

