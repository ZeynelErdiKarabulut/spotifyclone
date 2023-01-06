package com.example.spotifyclone.data.repositories.searchrepository

import androidx.paging.PagingData
import com.example.spotifyclone.data.utils.FetchedResource
import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.domain.spotifycloneErrorType
import com.example.spotifyclone.domain.SearchResult
import com.example.spotifyclone.domain.SearchResults
import kotlinx.coroutines.flow.Flow

/**
 * A repository that contains all methods related to searching.
 */
interface SearchRepository {
    suspend fun fetchSearchResultsForQuery(
        searchQuery: String,
        imageSize: MapperImageSize,
        countryCode: String
    ): FetchedResource<SearchResults, spotifycloneErrorType>

    fun getPaginatedSearchStreamForAlbums(
        searchQuery: String,
        countryCode: String,
        imageSize: MapperImageSize
    ): Flow<PagingData<SearchResult.AlbumSearchResult>>

    fun getPaginatedSearchStreamForArtists(
        searchQuery: String,
        countryCode: String,
        imageSize: MapperImageSize
    ): Flow<PagingData<SearchResult.ArtistSearchResult>>

    fun getPaginatedSearchStreamForTracks(
        searchQuery: String,
        countryCode: String,
        imageSize: MapperImageSize
    ): Flow<PagingData<SearchResult.TrackSearchResult>>

    fun getPaginatedSearchStreamForPlaylists(
        searchQuery: String,
        countryCode: String,
        imageSize: MapperImageSize
    ): Flow<PagingData<SearchResult.PlaylistSearchResult>>

    fun getPaginatedSearchStreamForPodcasts(
        searchQuery: String,
        countryCode: String,
        imageSize: MapperImageSize
    ): Flow<PagingData<SearchResult.PodcastSearchResult>>

    fun getPaginatedSearchStreamForEpisodes(
        searchQuery: String,
        countryCode: String,
        imageSize: MapperImageSize
    ): Flow<PagingData<SearchResult.EpisodeSearchResult>>
}