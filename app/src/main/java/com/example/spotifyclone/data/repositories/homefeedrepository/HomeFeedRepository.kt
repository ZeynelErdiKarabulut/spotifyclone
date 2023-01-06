package com.example.spotifyclone.data.repositories.homefeedrepository

import com.example.spotifyclone.data.utils.FetchedResource
import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.domain.FeaturedPlaylists
import com.example.spotifyclone.domain.spotifycloneErrorType
import com.example.spotifyclone.domain.PlaylistsForCategory
import com.example.spotifyclone.domain.SearchResult

/**
 * An interface that contains the requisite methods required for a repository
 * that contains methods for fetching items to be displayed in the home
 * feed.
 */
interface HomeFeedRepository {
    suspend fun fetchFeaturedPlaylistsForCurrentTimeStamp(
        timestampMillis: Long,
        countryCode: String,
        languageCode: ISO6391LanguageCode,
    ): FetchedResource<FeaturedPlaylists, spotifycloneErrorType>

    suspend fun fetchPlaylistsBasedOnCategoriesAvailableForCountry(
        countryCode: String,
        languageCode: ISO6391LanguageCode,
    ): FetchedResource<List<PlaylistsForCategory>, spotifycloneErrorType>

    suspend fun fetchNewlyReleasedAlbums(
        countryCode: String,
        mapperImageSize: MapperImageSize
    ): FetchedResource<List<SearchResult.AlbumSearchResult>, spotifycloneErrorType>
}