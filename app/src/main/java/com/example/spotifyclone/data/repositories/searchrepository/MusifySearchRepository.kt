package com.example.spotifyclone.data.repositories.searchrepository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.spotifyclone.data.paging.*
import com.example.spotifyclone.data.remote.musicservice.SpotifyService
import com.example.spotifyclone.data.remote.response.toSearchResults
import com.example.spotifyclone.data.repositories.tokenrepository.TokenRepository
import com.example.spotifyclone.data.repositories.tokenrepository.runCatchingWithToken
import com.example.spotifyclone.data.utils.FetchedResource
import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.domain.spotifycloneErrorType
import com.example.spotifyclone.domain.SearchResult
import com.example.spotifyclone.domain.SearchResults
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class spotifycloneSearchRepository @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val spotifyService: SpotifyService,
    private val pagingConfig: PagingConfig
) : SearchRepository {
    override suspend fun fetchSearchResultsForQuery(
        searchQuery: String,
        imageSize: MapperImageSize,
        countryCode: String
    ): FetchedResource<SearchResults, spotifycloneErrorType> = tokenRepository.runCatchingWithToken {
        spotifyService.search(searchQuery, countryCode, it).toSearchResults(imageSize)
    }

    override fun getPaginatedSearchStreamForAlbums(
        searchQuery: String,
        countryCode: String,
        imageSize: MapperImageSize
    ): Flow<PagingData<SearchResult.AlbumSearchResult>> = Pager(pagingConfig) {
        SpotifyAlbumSearchPagingSource(
            searchQuery = searchQuery,
            countryCode = countryCode,
            imageSize = imageSize,
            tokenRepository = tokenRepository,
            spotifyService = spotifyService
        )
    }.flow

    override fun getPaginatedSearchStreamForArtists(
        searchQuery: String,
        countryCode: String,
        imageSize: MapperImageSize
    ): Flow<PagingData<SearchResult.ArtistSearchResult>> = Pager(pagingConfig) {
        SpotifyArtistSearchPagingSource(
            searchQuery = searchQuery,
            countryCode = countryCode,
            imageSize = imageSize,
            tokenRepository = tokenRepository,
            spotifyService = spotifyService
        )
    }.flow

    override fun getPaginatedSearchStreamForTracks(
        searchQuery: String,
        countryCode: String,
        imageSize: MapperImageSize
    ): Flow<PagingData<SearchResult.TrackSearchResult>> = Pager(pagingConfig) {
        SpotifyTrackSearchPagingSource(
            searchQuery = searchQuery,
            countryCode = countryCode,
            imageSize = imageSize,
            tokenRepository = tokenRepository,
            spotifyService = spotifyService
        )
    }.flow

    override fun getPaginatedSearchStreamForPlaylists(
        searchQuery: String,
        countryCode: String,
        imageSize: MapperImageSize
    ): Flow<PagingData<SearchResult.PlaylistSearchResult>> = Pager(pagingConfig) {
        SpotifyPlaylistSearchPagingSource(
            searchQuery = searchQuery,
            countryCode = countryCode,
            imageSize = imageSize,
            tokenRepository = tokenRepository,
            spotifyService = spotifyService
        )
    }.flow

    override fun getPaginatedSearchStreamForPodcasts(
        searchQuery: String,
        countryCode: String,
        imageSize: MapperImageSize
    ): Flow<PagingData<SearchResult.PodcastSearchResult>> = Pager(pagingConfig) {
        SpotifyPodcastSearchPagingSource(
            searchQuery = searchQuery,
            countryCode = countryCode,
            imageSize = imageSize,
            tokenRepository = tokenRepository,
            spotifyService = spotifyService
        )
    }.flow

    override fun getPaginatedSearchStreamForEpisodes(
        searchQuery: String,
        countryCode: String,
        imageSize: MapperImageSize
    ): Flow<PagingData<SearchResult.EpisodeSearchResult>> = Pager(pagingConfig) {
        SpotifyEpisodeSearchPagingSource(
            searchQuery = searchQuery,
            countryCode = countryCode,
            imageSize = imageSize,
            tokenRepository = tokenRepository,
            spotifyService = spotifyService
        )
    }.flow
}
