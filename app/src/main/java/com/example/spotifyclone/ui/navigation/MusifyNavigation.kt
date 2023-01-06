package com.example.spotifyclone.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.spotifyclone.domain.HomeFeedCarouselCardInfo
import com.example.spotifyclone.domain.HomeFeedFilters
import com.example.spotifyclone.domain.SearchResult
import com.example.spotifyclone.domain.Streamable
import com.example.spotifyclone.ui.screens.GetPremiumScreen
import com.example.spotifyclone.ui.screens.homescreen.HomeScreen
import com.example.spotifyclone.ui.screens.searchscreen.PagingItemsForSearchScreen
import com.example.spotifyclone.ui.screens.searchscreen.SearchScreen
import com.example.spotifyclone.ui.theme.dynamictheme.DynamicBackgroundType
import com.example.spotifyclone.ui.theme.dynamictheme.DynamicThemeResource
import com.example.spotifyclone.ui.theme.dynamictheme.DynamicallyThemedSurface
import com.example.spotifyclone.viewmodels.homefeedviewmodel.HomeFeedViewModel
import com.example.spotifyclone.viewmodels.searchviewmodel.SearchFilter
import com.example.spotifyclone.viewmodels.searchviewmodel.SearchScreenUiState
import com.example.spotifyclone.viewmodels.searchviewmodel.SearchViewModel

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun spotifycloneNavigation(
    navController: NavHostController,
    playStreamable: (Streamable) -> Unit,
    isFullScreenNowPlayingOverlayScreenVisible: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = spotifycloneBottomNavigationDestinations.Home.route
    ) {
        navGraphWithDetailScreens(
            navGraphRoute = spotifycloneBottomNavigationDestinations.Home.route,
            startDestination = spotifycloneNavigationDestinations.HomeScreen.route,
            navController = navController,
            playStreamable = playStreamable
        ) { nestedController ->
            homeScreen(
                route = spotifycloneNavigationDestinations.HomeScreen.route,
                onCarouselCardClicked = {
                    nestedController.navigateToDetailScreen(searchResult = it.associatedSearchResult)
                }
            )
        }
        navGraphWithDetailScreens(
            navGraphRoute = spotifycloneBottomNavigationDestinations.Search.route,
            startDestination = spotifycloneNavigationDestinations.SearchScreen.route,
            navController = navController,
            playStreamable = playStreamable
        ) { nestedController ->
            searchScreen(
                route = spotifycloneNavigationDestinations.SearchScreen.route,
                onSearchResultClicked = nestedController::navigateToDetailScreen,
                isFullScreenNowPlayingScreenOverlayVisible = isFullScreenNowPlayingOverlayScreenVisible
            )
        }

        composable(spotifycloneBottomNavigationDestinations.Premium.route) {
            GetPremiumScreen()
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
private fun NavGraphBuilder.homeScreen(
    route: String,
    onCarouselCardClicked: (HomeFeedCarouselCardInfo) -> Unit
) {
    composable(route) {
        val homeFeedViewModel = hiltViewModel<HomeFeedViewModel>()
        val filters = remember {
            listOf(
                HomeFeedFilters.Music,
                HomeFeedFilters.PodcastsAndShows
            )
        }
        HomeScreen(
            timeBasedGreeting = homeFeedViewModel.greetingPhrase,
            homeFeedFilters = filters,
            currentlySelectedHomeFeedFilter = HomeFeedFilters.None,
            onHomeFeedFilterClick = {},
            carousels = homeFeedViewModel.homeFeedCarousels.value,
            onHomeFeedCarouselCardClick = onCarouselCardClicked,
            isErrorMessageVisible = homeFeedViewModel.uiState.value == HomeFeedViewModel.HomeFeedUiState.ERROR,
            isLoading = homeFeedViewModel.uiState.value == HomeFeedViewModel.HomeFeedUiState.LOADING,
            onErrorRetryButtonClick = homeFeedViewModel::refreshFeed
        )
    }
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
private fun NavGraphBuilder.searchScreen(
    route: String,
    onSearchResultClicked: (SearchResult) -> Unit,
    isFullScreenNowPlayingScreenOverlayVisible: Boolean,
) {
    composable(route = route) {
        val viewModel = hiltViewModel<SearchViewModel>()
        val albums = viewModel.albumListForSearchQuery.collectAsLazyPagingItems()
        val artists = viewModel.artistListForSearchQuery.collectAsLazyPagingItems()
        val playlists = viewModel.playlistListForSearchQuery.collectAsLazyPagingItems()
        val tracks = viewModel.trackListForSearchQuery.collectAsLazyPagingItems()
        val podcasts = viewModel.podcastListForSearchQuery.collectAsLazyPagingItems()
        val episodes = viewModel.episodeListForSearchQuery.collectAsLazyPagingItems()
        val pagingItems = remember {
            PagingItemsForSearchScreen(
                albums,
                artists,
                tracks,
                playlists,
                podcasts,
                episodes
            )
        }
        val uiState by viewModel.uiState
        val isLoadingError by remember {
            derivedStateOf {
                tracks.loadState.refresh is LoadState.Error || tracks.loadState.append is LoadState.Error || tracks.loadState.prepend is LoadState.Error
            }
        }
        val controller = LocalSoftwareKeyboardController.current
        val genres = remember { viewModel.getAvailableGenres() }
        val filters = remember { SearchFilter.values().toList() }
        val currentlySelectedFilter by viewModel.currentlySelectedFilter
        val dynamicThemeResource by remember {
            derivedStateOf {
                val imageUrl = when (currentlySelectedFilter) {
                    SearchFilter.ALBUMS -> albums.itemSnapshotList.firstOrNull()?.albumArtUrlString
                    SearchFilter.TRACKS -> tracks.itemSnapshotList.firstOrNull()?.imageUrlString
                    SearchFilter.ARTISTS -> artists.itemSnapshotList.firstOrNull()?.imageUrlString
                    SearchFilter.PLAYLISTS -> playlists.itemSnapshotList.firstOrNull()?.imageUrlString
                    SearchFilter.PODCASTS  -> podcasts.itemSnapshotList.firstOrNull()?.imageUrlString
                }
                if (imageUrl == null) DynamicThemeResource.Empty
                else DynamicThemeResource.FromImageUrl(imageUrl)
            }
        }
        val currentlyPlayingTrack by viewModel.currentlyPlayingTrackStream.collectAsState(initial = null)
        DynamicallyThemedSurface(
            dynamicThemeResource = dynamicThemeResource,
            dynamicBackgroundType = DynamicBackgroundType.Gradient()
        ) {
            SearchScreen(
                genreList = genres,
                searchScreenFilters = filters,
                onGenreItemClick = {},
                onSearchTextChanged = viewModel::search,
                isLoading = uiState == SearchScreenUiState.LOADING,
                pagingItems = pagingItems,
                onSearchQueryItemClicked = onSearchResultClicked,
                currentlySelectedFilter = viewModel.currentlySelectedFilter.value,
                onSearchFilterChanged = viewModel::updateSearchFilter,
                isSearchErrorMessageVisible = isLoadingError,
                onImeDoneButtonClicked = {
                    // Search only if there was an error while loading.
                    // A manual call to search() is not required
                    // when there is no error because, search()
                    // will be called automatically, everytime the
                    // search text changes. This prevents duplicate
                    // calls when the user manually clicks the done
                    // button after typing the search text, in
                    // which case, the keyboard will just be hidden.
                    if (isLoadingError) viewModel.search(it)
                    controller?.hide()
                },
                currentlyPlayingTrack = currentlyPlayingTrack,
                isFullScreenNowPlayingOverlayScreenVisible = isFullScreenNowPlayingScreenOverlayVisible,
                onErrorRetryButtonClick = viewModel::search
            )
        }
    }
}
