package com.example.spotifyclone.data.repositories.podcastsrepository

import com.example.spotifyclone.data.encoder.TestBase64Encoder
import com.example.spotifyclone.data.remote.musicservice.SpotifyService
import com.example.spotifyclone.data.remote.token.tokenmanager.TokenManager
import com.example.spotifyclone.data.repositories.tokenrepository.SpotifyTokenRepository
import com.example.spotifyclone.data.utils.FetchedResource
import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.utils.defaultspotifycloneJacksonConverterFactory
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class spotifyclonePodcastsRepositoryTest {
    private lateinit var podcastsRepository: PodcastsRepository

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
        podcastsRepository = spotifyclonePodcastsRepository(
            tokenRepository = SpotifyTokenRepository(
                tokenManager,
                TestBase64Encoder()
            ),
            spotifyService = spotifyService
        )
    }

    @Test
    fun fetchPodcastEpisodeTest_validEpisodeId_successfullyFetchesPodcastEpisode() = runBlocking {
        val validEpisodeId = "5pLYyCItRvIc2SEbuJ3eO8"
        val fetchedResource = podcastsRepository.fetchPodcastEpisode(
            episodeId = validEpisodeId,
            countryCode = "IN",
            imageSize = MapperImageSize.SMALL
        )
        assert(fetchedResource is FetchedResource.Success)
    }

}