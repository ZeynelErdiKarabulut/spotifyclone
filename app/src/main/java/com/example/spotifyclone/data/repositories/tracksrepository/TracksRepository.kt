package com.example.spotifyclone.data.repositories.tracksrepository

import androidx.paging.PagingData
import com.example.spotifyclone.data.utils.FetchedResource
import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.domain.Genre
import com.example.spotifyclone.domain.spotifycloneErrorType
import com.example.spotifyclone.domain.SearchResult
import kotlinx.coroutines.flow.Flow

/**
 * A repository that contains methods related to tracks. **All methods
 * of this interface will always return an instance of [SearchResult.TrackSearchResult]**
 * encapsulated inside [FetchedResource.Success] if the resource was
 * fetched successfully. This ensures that the return value of all the
 * methods of [TracksRepository] will always return [SearchResult.TrackSearchResult]
 * in the case of a successful fetch operation.
 */
interface TracksRepository {
    suspend fun fetchTopTenTracksForArtistWithId(
        artistId: String,
        imageSize: MapperImageSize,
        countryCode: String
    ): FetchedResource<List<SearchResult.TrackSearchResult>, spotifycloneErrorType>

    suspend fun fetchTracksForGenre(
        genre: Genre,
        imageSize: MapperImageSize,
        countryCode: String
    ): FetchedResource<List<SearchResult.TrackSearchResult>, spotifycloneErrorType>

    suspend fun fetchTracksForAlbumWithId(
        albumId: String,
        countryCode: String,
        imageSize: MapperImageSize
    ): FetchedResource<List<SearchResult.TrackSearchResult>, spotifycloneErrorType>

    fun getPaginatedStreamForPlaylistTracks(
        playlistId: String,
        countryCode: String,
        imageSize: MapperImageSize
    ): Flow<PagingData<SearchResult.TrackSearchResult>>
}