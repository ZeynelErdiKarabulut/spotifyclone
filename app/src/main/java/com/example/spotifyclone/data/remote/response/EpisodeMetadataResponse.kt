package com.example.spotifyclone.data.remote.response

import com.example.spotifyclone.data.utils.MapperImageSize
import com.example.spotifyclone.data.utils.getFormattedEpisodeReleaseDateAndDuration
import com.example.spotifyclone.data.utils.getImageResponseForImageSize
import com.example.spotifyclone.domain.SearchResult
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A response object that contains metadata of a specific episode.
 */
data class EpisodeMetadataResponse(
    val id: String,
    @JsonProperty("name") val title: String,
    val description: String,
    @JsonProperty("duration_ms") val durationMillis: Long,
    val images: List<ImageResponse>,
    @JsonProperty("release_date") val releaseDate: String
)

/**
 * A mapper function used to map an instance of [EpisodeMetadataResponse] to
 * an instance of [SearchResult.EpisodeSearchResult]. The [imageSize]
 * parameter determines the size of image to be used for the
 * [SearchResult.EpisodeSearchResult] instance.
 * Note: The [SearchResult.EpisodeSearchResult.EpisodeDurationInfo.minutes]
 * is guaranteed to have a minimum value of 1. This means that any episode
 * with a duration lower than 1 minute will be coerced to have a value of
 * 1 minute.
 */
fun EpisodeMetadataResponse.toEpisodeSearchResult(imageSize: MapperImageSize): SearchResult.EpisodeSearchResult {
    val contentInfo = SearchResult.EpisodeSearchResult.EpisodeContentInfo(
        title = this.title,
        description = this.description,
        imageUrlString = images.getImageResponseForImageSize(imageSize).url
    )
    val formattedEpisodeReleaseDateAndDuration = getFormattedEpisodeReleaseDateAndDuration(
        releaseDateString = this.releaseDate,
        durationMillis = this.durationMillis
    )
    val episodeDateInfo = SearchResult.EpisodeSearchResult.EpisodeReleaseDateInfo(
        month = formattedEpisodeReleaseDateAndDuration.month,
        day = formattedEpisodeReleaseDateAndDuration.day,
        year = formattedEpisodeReleaseDateAndDuration.year,
    )
    val episodeDurationInfo = SearchResult.EpisodeSearchResult.EpisodeDurationInfo(
        hours = formattedEpisodeReleaseDateAndDuration.hours,
        minutes = formattedEpisodeReleaseDateAndDuration.minutes
    )

    return SearchResult.EpisodeSearchResult(
        id = this.id,
        episodeContentInfo = contentInfo,
        episodeReleaseDateInfo = episodeDateInfo,
        episodeDurationInfo = episodeDurationInfo
    )
}
