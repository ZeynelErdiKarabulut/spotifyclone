package com.example.spotifyclone.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.spotifyclone.R
import com.example.spotifyclone.domain.PodcastEpisode
import com.example.spotifyclone.domain.SearchResult
import com.example.spotifyclone.domain.Streamable
import com.example.spotifyclone.ui.theme.dynamictheme.DynamicBackgroundType
import com.example.spotifyclone.ui.theme.dynamictheme.DynamicThemeResource
import com.example.spotifyclone.ui.theme.dynamictheme.DynamicallyThemedSurface

/**
 * An object that contains constants related to the [spotifycloneMiniPlayer]
 * composable.
 */
object spotifycloneMiniPlayerConstants {
    val miniPlayerHeight = 60.dp
}

/**
 * A mini player composable.
 * It also displays 2 icons - Available Devices, and Play/Pause.
 *
 * Note: The size of this composable is **fixed to 60dp**.
 *
 * @param streamable the currently [Streamable].
 * @param isPlaybackPaused indicates whether the playback is paused.
 * Based on this, either [onPlayButtonClicked] or [onPauseButtonClicked]
 * will be invoked. Also, the play and pause icons will also be displayed
 * based on this parameter.
 * @param modifier the modifier to be applied to this composable.
 * @param onLikedButtonClicked the lambda to execute when the like
 * button is clicked. It is provided with a boolean that indicates
 * whether the the track is currently liked or not.
 * @param onPlayButtonClicked the lambda to execute when the play button
 * is clicked.
 * @param onPauseButtonClicked the lambda to execute when the pause button
 * is clicked.
 */
// TODO Make text scrollable if it overflows
// TODO debug recompositions
@Composable
fun spotifycloneMiniPlayer(
    streamable: Streamable,
    isPlaybackPaused: Boolean,
    modifier: Modifier = Modifier,
    onLikedButtonClicked: (Boolean) -> Unit,
    onPlayButtonClicked: () -> Unit,
    onPauseButtonClicked: () -> Unit
) {
    var isThumbnailImageLoading by remember { mutableStateOf(false) }
    val title = remember(streamable) {
        when (streamable) {
            is PodcastEpisode -> streamable.title
            is SearchResult.TrackSearchResult -> streamable.name
        }

    }
    val subtitle = remember(streamable) {
        when (streamable) {
            is PodcastEpisode -> streamable.podcastInfo.name
            is SearchResult.TrackSearchResult -> streamable.artistsString
        }
    }
    val imageUrl = remember(streamable) {
        when (streamable) {
            is PodcastEpisode -> streamable.podcastInfo.imageUrl
            is SearchResult.TrackSearchResult -> streamable.imageUrlString
        }
    }
    val dynamicThemeResource = remember(imageUrl) {
        DynamicThemeResource.FromImageUrl(imageUrl)
    }
    DynamicallyThemedSurface(
        modifier = Modifier
            .then(modifier)
            .heightIn(
                spotifycloneMiniPlayerConstants.miniPlayerHeight,
                spotifycloneMiniPlayerConstants.miniPlayerHeight
            ) // the height of this composable is fixed
            .clip(RoundedCornerShape(8.dp)),
        dynamicThemeResource = dynamicThemeResource,
        dynamicBackgroundType = DynamicBackgroundType.Filled(scrimColor = Color.Black.copy(0.6f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImageWithPlaceholder(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .aspectRatio(1f),
                model = imageUrl,
                contentDescription = null,
                onImageLoadingFinished = { isThumbnailImageLoading = false },
                isLoadingPlaceholderVisible = isThumbnailImageLoading,
                onImageLoading = { isThumbnailImageLoading = true },
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(

                    text = title,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    text = subtitle,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.caption.copy(
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f)
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_available_devices),
                    contentDescription = null
                )
            }
            IconButton(onClick = {
                if (isPlaybackPaused) {
                    // if the playback is paused, then the play button
                    // would be visible. Hence, invoke the lambda that
                    // is required to be executed when the play button
                    // is visible.
                    onPlayButtonClicked()
                } else {
                    // Similarly, if the track is being played, then the pause
                    // button would be visible. Hence, invoke the lambda that
                    // is required to be executed when the pause button
                    // is visible.
                    onPauseButtonClicked()
                }
            }) {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .aspectRatio(1f),
                    painter = if (isPlaybackPaused) painterResource(R.drawable.ic_play_arrow_24)
                    else painterResource(R.drawable.ic_pause_24),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}
