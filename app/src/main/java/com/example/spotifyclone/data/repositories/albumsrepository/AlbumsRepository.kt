package com.example.spotifyclone.data.repositories.albumsrepository

import androidx.paging.PagingData
import com.example.spotifyclone.data.utils.FetchedResource
import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.domain.spotifycloneErrorType
import com.example.spotifyclone.domain.SearchResult
import kotlinx.coroutines.flow.Flow

/**
 * A repository that contains methods related to albums. **All methods
 * of this interface will always return an instance of [SearchResult.AlbumSearchResult]**
 * encapsulated inside [FetchedResource.Success] if the resource was
 * fetched successfully. This ensures that the return value of all the
 * methods of [AlbumsRepository] will always return [SearchResult.AlbumSearchResult]
 * in the case of a successful fetch operation.
 */
interface AlbumsRepository {
    suspend fun fetchAlbumWithId(
        albumId: String,
        imageSize: MapperImageSize,
        countryCode: String
    ): FetchedResource<SearchResult.AlbumSearchResult, spotifycloneErrorType>

    suspend fun fetchAlbumsOfArtistWithId(
        artistId: String,
        imageSize: MapperImageSize,
        countryCode: String
    ): FetchedResource<List<SearchResult.AlbumSearchResult>, spotifycloneErrorType>

    fun getPaginatedStreamForAlbumsOfArtist(
        artistId: String,
        countryCode: String,
        imageSize: MapperImageSize
    ): Flow<PagingData<SearchResult.AlbumSearchResult>>
}
