package com.example.spotifyclone.data.repositories.artistsrepository

import com.example.spotifyclone.data.remote.musicservice.SpotifyService
import com.example.spotifyclone.data.remote.response.toArtistSearchResult
import com.example.spotifyclone.data.repositories.tokenrepository.TokenRepository
import com.example.spotifyclone.data.repositories.tokenrepository.runCatchingWithToken
import com.example.spotifyclone.data.utils.FetchedResource
import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.domain.spotifycloneErrorType
import com.example.spotifyclone.domain.SearchResult
import javax.inject.Inject

class spotifycloneArtistsRepository @Inject constructor(
    private val spotifyService: SpotifyService,
    private val tokenRepository: TokenRepository
) : ArtistsRepository {

    override suspend fun fetchArtistSummaryForId(
        artistId: String,
        imageSize: MapperImageSize
    ): FetchedResource<SearchResult.ArtistSearchResult, spotifycloneErrorType> =
        tokenRepository.runCatchingWithToken {
            spotifyService.getArtistInfoWithId(artistId, it).toArtistSearchResult(imageSize)
        }
}