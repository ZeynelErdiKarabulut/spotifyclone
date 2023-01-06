package com.example.spotifyclone.data.repositories.artistsrepository

import com.example.spotifyclone.data.utils.FetchedResource
import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.domain.spotifycloneErrorType
import com.example.spotifyclone.domain.SearchResult

/**
 * A repository that contains methods related to artists. **All methods
 * of this interface will always return an instance of [SearchResult.ArtistSearchResult]**
 * encapsulated inside [FetchedResource.Success] if the resource was
 * fetched successfully. This ensures that the return value of all the
 * methods of [ArtistsRepository] will always return [SearchResult.ArtistSearchResult]
 * in the case of a successful fetch operation.
 */
interface ArtistsRepository {
    suspend fun fetchArtistSummaryForId(
        artistId: String,
        imageSize: MapperImageSize
    ): FetchedResource<SearchResult.ArtistSearchResult, spotifycloneErrorType>

}