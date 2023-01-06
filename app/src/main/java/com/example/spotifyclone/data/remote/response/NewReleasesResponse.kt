package com.example.spotifyclone.data.remote.response

import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.domain.SearchResult

/**
 * A response object that contains [albums] that were newly released.
 */
data class NewReleasesResponse(val albums: Albums) {
    /**
     * A response object that contains a list of [AlbumMetadataResponse].
     */
    data class Albums(val items: List<AlbumMetadataResponse>)
}

/**
 * A mapper method used to map an instance of [NewReleasesResponse] to
 * a list of [SearchResult.AlbumSearchResult].
 */
fun NewReleasesResponse.toAlbumSearchResultList(size: MapperImageSize): List<SearchResult.AlbumSearchResult> =
    this.albums.items.map { it.toAlbumSearchResult(size) }