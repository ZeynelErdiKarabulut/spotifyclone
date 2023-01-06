package com.example.spotifyclone.data.repositories.artistsrepository

import com.example.spotifyclone.data.encoder.TestBase64Encoder
import com.example.spotifyclone.data.remote.musicservice.SpotifyService
import com.example.spotifyclone.data.remote.token.tokenmanager.TokenManager
import com.example.spotifyclone.data.repositories.tokenrepository.SpotifyTokenRepository
import com.example.spotifyclone.data.utils.FetchedResource
import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.domain.spotifycloneErrorType
import com.example.spotifyclone.utils.defaultspotifycloneJacksonConverterFactory
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class spotifycloneArtistsRepositoryTest {

    private lateinit var artistsRepository: spotifycloneArtistsRepository

    @Before
    fun setUp() {
        val spotifyService = Retrofit.Builder()
            .baseUrl("https://api.spotify.com/")
            .addConverterFactory(defaultspotifycloneJacksonConverterFactory)
            .build()
            .create(SpotifyService::class.java)
        val tokenManager = Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com/")
            .addConverterFactory(defaultspotifycloneJacksonConverterFactory)
            .build()
            .create(TokenManager::class.java)
        artistsRepository = spotifycloneArtistsRepository(
            tokenRepository = SpotifyTokenRepository(
                tokenManager,
                TestBase64Encoder()
            ),
            spotifyService = spotifyService
        )
    }

    @Test
    fun artistFetchTest_validArtistId_isFetchedSuccessfully() = runBlocking {
        // given a valid artist id
        val validArtistId = "4zCH9qm4R2DADamUHMCa6O" // Anirudh Ravichander
        // when fetching the artist summary using the id
        val resource =
            artistsRepository.fetchArtistSummaryForId(validArtistId, MapperImageSize.SMALL)
        // the return type must be of type FetchedResource.Success
        assert(resource is FetchedResource.Success)
    }

    @Test
    fun artistFetchTest_invalidArtistId_returnsFailedFetchedResource() = runBlocking {
        // given an invalid artist id
        val invalid = "-"
        // when fetching the artist summary using the id
        val resource =
            artistsRepository.fetchArtistSummaryForId(invalid, MapperImageSize.SMALL)
        // the return type must be of type FetchedResource.Failure
        assert(resource is FetchedResource.Failure)
        // the error type must be spotifycloneHttpErrorType.INVALID_REQUEST
        assert((resource as FetchedResource.Failure).cause == spotifycloneErrorType.INVALID_REQUEST)
    }

}