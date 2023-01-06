package com.example.spotifyclone.domain

import android.content.Context
import android.text.Spanned
import com.example.spotifyclone.utils.generatespotifycloneDateAndDurationString

/**
 * A domain class that represents a specific podcast episode.
 */
data class PodcastEpisode(
    val id: String,
    val title: String,
    val description: String,
    val htmlDescription: Spanned,
    val previewUrl: String?,
    val releaseDateInfo: ReleaseDateInfo,
    val durationInfo: DurationInfo,
    val podcastInfo: PodcastInfo
) : Streamable {
    override val streamUrl: String? = previewUrl

    /**
     * A domain class that contains the associated podcast information of a
     * [PodcastEpisode].
     */
    data class PodcastInfo(
        val id: String,
        val name: String,
        val imageUrl: String
    )

    /**
     * A domain class that contains the date information of a [PodcastEpisode].
     */
    data class ReleaseDateInfo(val month: String, val day: Int, val year: Int)

    /**
     * A domain class that contains the duration information of a [PodcastEpisode].
     */
    data class DurationInfo(val hours: Int, val minutes: Int)
}

/**
 * A utility method used to get a string that contains date and duration
 * information in a formatted manner.
 * @see generatespotifycloneDateAndDurationString
 */
fun PodcastEpisode.getFormattedDateAndDurationString(context: Context): String =
    generatespotifycloneDateAndDurationString(
        context = context,
        month = releaseDateInfo.month,
        day = releaseDateInfo.day,
        year = releaseDateInfo.year,
        hours = durationInfo.hours,
        minutes = durationInfo.minutes
    )