package com.example.spotifyclone.data.repositories.homefeedrepository

import com.example.spotifyclone.data.remote.musicservice.SpotifyService
import com.example.spotifyclone.data.remote.response.toAlbumSearchResultList
import com.example.spotifyclone.data.remote.response.toFeaturedPlaylists
import com.example.spotifyclone.data.remote.response.toPlaylistSearchResultList
import com.example.spotifyclone.data.repositories.tokenrepository.TokenRepository
import com.example.spotifyclone.data.repositories.tokenrepository.runCatchingWithToken
import com.example.spotifyclone.data.utils.FetchedResource
import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.domain.FeaturedPlaylists
import com.example.spotifyclone.domain.spotifycloneErrorType
import com.example.spotifyclone.domain.PlaylistsForCategory
import com.example.spotifyclone.domain.SearchResult
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class spotifycloneHomeFeedRepository @Inject constructor(
    private val spotifyService: SpotifyService,
    private val tokenRepository: TokenRepository
) : HomeFeedRepository {
    override suspend fun fetchNewlyReleasedAlbums(
        countryCode: String,
        mapperImageSize: MapperImageSize
    ): FetchedResource<List<SearchResult.AlbumSearchResult>, spotifycloneErrorType> =
        tokenRepository.runCatchingWithToken { token ->
            spotifyService
                .getNewReleases(token = token, market = countryCode)
                .toAlbumSearchResultList(mapperImageSize)
        }


    override suspend fun fetchFeaturedPlaylistsForCurrentTimeStamp(
        timestampMillis: Long,
        countryCode: String,
        languageCode: ISO6391LanguageCode,
    ): FetchedResource<FeaturedPlaylists, spotifycloneErrorType> =
        tokenRepository.runCatchingWithToken { token ->
            val timestamp = ISODateTimeString.from(timestampMillis)
            spotifyService.getFeaturedPlaylists(
                token = token,
                market = countryCode,
                locale = "${languageCode.value}_$countryCode",
                timestamp = timestamp
            ).toFeaturedPlaylists()
        }

    override suspend fun fetchPlaylistsBasedOnCategoriesAvailableForCountry(
        countryCode: String,
        languageCode: ISO6391LanguageCode,
    ): FetchedResource<List<PlaylistsForCategory>, spotifycloneErrorType> =
        tokenRepository.runCatchingWithToken { token ->
            val locale = "${languageCode.value}_$countryCode"
            val categories = spotifyService.getBrowseCategories(
                token = token,
                market = countryCode,
                locale = locale
            ).categories.items
            coroutineScope {
                // instead of fetching playlists for each category in a sequential manner
                // fetch it in parallel
                val playlistsMap = categories.map { huh ->
                    async {
                        spotifyService.getPlaylistsForCategory(
                            token = token,
                            categoryId = huh.id,
                            market = countryCode
                        ).toPlaylistSearchResultList()
                    }
                }
                playlistsMap.awaitAll().mapIndexed { index, playlists ->
                    PlaylistsForCategory(
                        categoryId = categories[index].id,
                        nameOfCategory = categories[index].name,
                        associatedPlaylists = playlists
                    )
                }
            }
        }
}
