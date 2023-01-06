package com.example.spotifyclone.data.repositories.tracksrepository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.spotifyclone.data.paging.PlaylistTracksPagingSource
import com.example.spotifyclone.data.remote.musicservice.SpotifyService
import com.example.spotifyclone.data.remote.response.getTracks
import com.example.spotifyclone.data.remote.response.toTrackSearchResult
import com.example.spotifyclone.data.repositories.tokenrepository.TokenRepository
import com.example.spotifyclone.data.repositories.tokenrepository.runCatchingWithToken
import com.example.spotifyclone.data.utils.FetchedResource
import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.domain.Genre
import com.example.spotifyclone.domain.spotifycloneErrorType
import com.example.spotifyclone.domain.SearchResult
import com.example.spotifyclone.domain.toSupportedSpotifyGenreType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class spotifycloneTracksRepository @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val spotifyService: SpotifyService,
    private val pagingConfig: PagingConfig
) : TracksRepository {
    override suspend fun fetchTopTenTracksForArtistWithId(
        artistId: String,
        imageSize: MapperImageSize,
        countryCode: String
    ): FetchedResource<List<SearchResult.TrackSearchResult>, spotifycloneErrorType> =
        tokenRepository.runCatchingWithToken {
            spotifyService.getTopTenTracksForArtistWithId(
                artistId = artistId,
                market = countryCode,
                token = it,
            ).value.map { trackDTOWithAlbumMetadata ->
                trackDTOWithAlbumMetadata.toTrackSearchResult(imageSize)
            }
        }

    override suspend fun fetchTracksForGenre(
        genre: Genre,
        imageSize: MapperImageSize,
        countryCode: String
    ): FetchedResource<List<SearchResult.TrackSearchResult>, spotifycloneErrorType> =
        tokenRepository.runCatchingWithToken {
            spotifyService.getTracksForGenre(
                genre = genre.genreType.toSupportedSpotifyGenreType(),
                market = countryCode,
                token = it
            ).value.map { trackDTOWithAlbumMetadata ->
                trackDTOWithAlbumMetadata.toTrackSearchResult(imageSize)
            }
        }

    override suspend fun fetchTracksForAlbumWithId(
        albumId: String,
        countryCode: String,
        imageSize: MapperImageSize
    ): FetchedResource<List<SearchResult.TrackSearchResult>, spotifycloneErrorType> =
        tokenRepository.runCatchingWithToken {
            spotifyService.getAlbumWithId(albumId, countryCode, it).getTracks(imageSize)
        }

    override fun getPaginatedStreamForPlaylistTracks(
        playlistId: String,
        countryCode: String,
        imageSize: MapperImageSize
    ): Flow<PagingData<SearchResult.TrackSearchResult>> = Pager(pagingConfig) {
        PlaylistTracksPagingSource(
            playlistId = playlistId,
            countryCode = countryCode,
            imageSize = imageSize,
            tokenRepository = tokenRepository,
            spotifyService = spotifyService
        )
    }.flow
}