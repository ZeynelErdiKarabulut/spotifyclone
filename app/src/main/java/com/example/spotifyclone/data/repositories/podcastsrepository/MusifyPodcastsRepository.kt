package com.example.spotifyclone.data.repositories.podcastsrepository

import com.example.spotifyclone.data.remote.musicservice.SpotifyService
import com.example.spotifyclone.data.remote.response.toPodcastEpisode
import com.example.spotifyclone.data.repositories.tokenrepository.TokenRepository
import com.example.spotifyclone.data.repositories.tokenrepository.runCatchingWithToken
import com.example.spotifyclone.data.utils.FetchedResource
import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.domain.spotifycloneErrorType
import com.example.spotifyclone.domain.PodcastEpisode
import javax.inject.Inject

class spotifyclonePodcastsRepository @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val spotifyService: SpotifyService
) : PodcastsRepository {

    override suspend fun fetchPodcastEpisode(
        episodeId: String,
        countryCode: String,
        imageSize: MapperImageSize,
    ): FetchedResource<PodcastEpisode, spotifycloneErrorType> = tokenRepository.runCatchingWithToken {
        spotifyService.getEpisodeWithId(
            token = it,
            id = episodeId,
            market = countryCode
        ).toPodcastEpisode(imageSize)
    }
}