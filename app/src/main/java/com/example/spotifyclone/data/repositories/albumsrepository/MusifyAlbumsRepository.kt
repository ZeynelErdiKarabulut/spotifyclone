package com.example.spotifyclone.data.repositories.albumsrepository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.spotifyclone.data.paging.AlbumsOfArtistPagingSource
import com.example.spotifyclone.data.remote.musicservice.SpotifyService
import com.example.spotifyclone.data.remote.response.toAlbumSearchResult
import com.example.spotifyclone.data.remote.response.toAlbumSearchResultList
import com.example.spotifyclone.data.repositories.tokenrepository.TokenRepository
import com.example.spotifyclone.data.repositories.tokenrepository.runCatchingWithToken
import com.example.spotifyclone.data.utils.FetchedResource
import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.domain.spotifycloneErrorType
import com.example.spotifyclone.domain.SearchResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class spotifycloneAlbumsRepository @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val spotifyService: SpotifyService,
    private val pagingConfig: PagingConfig
) : AlbumsRepository {

    override suspend fun fetchAlbumsOfArtistWithId(
        artistId: String,
        imageSize: MapperImageSize,
        countryCode: String, //ISO 3166-1 alpha-2 country code
    ): FetchedResource<List<SearchResult.AlbumSearchResult>, spotifycloneErrorType> =
        tokenRepository.runCatchingWithToken {
            spotifyService.getAlbumsOfArtistWithId(
                artistId,
                countryCode,
                it
            ).toAlbumSearchResultList(imageSize)
        }

    override suspend fun fetchAlbumWithId(
        albumId: String,
        imageSize: MapperImageSize,
        countryCode: String
    ): FetchedResource<SearchResult.AlbumSearchResult, spotifycloneErrorType> =
        tokenRepository.runCatchingWithToken {
            spotifyService.getAlbumWithId(albumId, countryCode, it).toAlbumSearchResult(imageSize)
        }

    override fun getPaginatedStreamForAlbumsOfArtist(
        artistId: String,
        countryCode: String,
        imageSize: MapperImageSize
    ): Flow<PagingData<SearchResult.AlbumSearchResult>> = Pager(pagingConfig) {
        AlbumsOfArtistPagingSource(
            artistId = artistId,
            market = countryCode,
            mapperImageSize = imageSize,
            tokenRepository = tokenRepository,
            spotifyService = spotifyService
        )
    }.flow
}