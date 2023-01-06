package com.example.spotifyclone.data.repositories.tracksrepository

import com.example.spotifyclone.data.encoder.TestBase64Encoder
import com.example.spotifyclone.data.remote.musicservice.SpotifyService
import com.example.spotifyclone.data.remote.token.tokenmanager.TokenManager
import com.example.spotifyclone.data.repositories.tokenrepository.SpotifyTokenRepository
import com.example.spotifyclone.data.utils.FetchedResource
import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.di.PagingConfigModule
import com.example.spotifyclone.domain.Genre
import com.example.spotifyclone.utils.defaultspotifycloneJacksonConverterFactory
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class spotifycloneTracksRepositoryTest {

    private lateinit var spotifycloneTracksRepository: spotifycloneTracksRepository

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
        spotifycloneTracksRepository = spotifycloneTracksRepository(
            tokenRepository = SpotifyTokenRepository(
                tokenManager,
                TestBase64Encoder()
            ),
            spotifyService = spotifyService,
            pagingConfig = PagingConfigModule.provideDefaultPagingConfig()
        )
    }


    @Test
    fun fetchTopTenTracksOfArtist_validArtistId_isSuccessfullyFetched() {
        // given a valid artist id
        val validArtistId = "4zCH9qm4R2DADamUHMCa6O" // Anirudh Ravichander
        val countryCode = "IN"
        // when fetching the top ten tracks of the artist
        val result = runBlocking {
            spotifycloneTracksRepository.fetchTopTenTracksForArtistWithId(
                validArtistId,
                MapperImageSize.SMALL,
                countryCode
            )
        }
        // the return type must be of type FetchedResource.Success
        assert(result is FetchedResource.Success)
        // the list should not be empty
        assert((result as FetchedResource.Success).data.isNotEmpty())
    }

    @Test
    fun fetchTracksForGenreTest_validGenre_tracksSuccessfullyFetched() {
        val validGenre = Genre(
            id = "",
            label = "",
            genreType = Genre.GenreType.AMBIENT
        )
        val result = runBlocking {
            spotifycloneTracksRepository.fetchTracksForGenre(
                validGenre,
                MapperImageSize.SMALL,
                "IN"
            )
        }
        assert(result is FetchedResource.Success)
        assert((result as FetchedResource.Success).data.isNotEmpty())
    }

    @Test
    fun fetchTracksForAlbumTest_validAlbumId_associatedTracksSuccessfullyFetched() {
        val validGenreId = "1ftvBBcu7jYIvXyt3JWB8S" // "The Eminem Show"
        val result = runBlocking {
            spotifycloneTracksRepository.fetchTracksForAlbumWithId(
                albumId = validGenreId,
                countryCode = "IN",
                imageSize = MapperImageSize.SMALL
            )
        }
        assert(result is FetchedResource.Success)
        assert((result as FetchedResource.Success).data.isNotEmpty())
        println(result.data)
    }
}
