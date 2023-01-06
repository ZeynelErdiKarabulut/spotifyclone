package com.example.spotifyclone.data.repositories.searchrepository

import com.example.spotifyclone.data.encoder.TestBase64Encoder
import com.example.spotifyclone.data.remote.musicservice.SpotifyService
import com.example.spotifyclone.data.remote.token.tokenmanager.TokenManager
import com.example.spotifyclone.data.repositories.tokenrepository.SpotifyTokenRepository
import com.example.spotifyclone.data.utils.FetchedResource
import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.di.PagingConfigModule
import com.example.spotifyclone.utils.defaultspotifycloneJacksonConverterFactory
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class spotifycloneSearchRepositoryTest {
    private lateinit var spotifycloneSearchRepository: spotifycloneSearchRepository

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
        spotifycloneSearchRepository = spotifycloneSearchRepository(
            tokenRepository = SpotifyTokenRepository(
                tokenManager,
                TestBase64Encoder()
            ),
            spotifyService = spotifyService,
            pagingConfig = PagingConfigModule.provideDefaultPagingConfig()
        )
    }

    @Test
    fun fetchSearchResultsTest_validSearchQuery_isSuccessfullyFetched() {
        // given an valid search query
        val query = "Dull Knives"
        // when fetching search results for the query
        val result = runBlocking {
            spotifycloneSearchRepository.fetchSearchResultsForQuery(query, MapperImageSize.SMALL, "IN")
        }
        // the return type must be of type FetchedResource.Success
        assert(result is FetchedResource.Success)
    }
}