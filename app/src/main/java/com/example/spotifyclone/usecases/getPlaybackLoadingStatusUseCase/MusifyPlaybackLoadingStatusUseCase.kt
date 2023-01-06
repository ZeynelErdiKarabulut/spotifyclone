package com.example.spotifyclone.usecases.getPlaybackLoadingStatusUseCase

import com.example.spotifyclone.musicplayer.MusicPlayerV2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class spotifycloneGetPlaybackLoadingStatusUseCase @Inject constructor(
    musicPlayerV2: MusicPlayerV2
):GetPlaybackLoadingStatusUseCase {
    override val loadingStatusStream: Flow<Boolean> = musicPlayerV2.currentPlaybackStateStream
        .map { it is MusicPlayerV2.PlaybackState.Loading }
        .distinctUntilChanged()
}