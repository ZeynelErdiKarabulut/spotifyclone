package com.example.spotifyclone.di

import com.example.spotifyclone.data.repositories.albumsrepository.AlbumsRepository
import com.example.spotifyclone.data.repositories.albumsrepository.spotifycloneAlbumsRepository
import com.example.spotifyclone.data.repositories.artistsrepository.ArtistsRepository
import com.example.spotifyclone.data.repositories.artistsrepository.spotifycloneArtistsRepository
import com.example.spotifyclone.data.repositories.genresrepository.GenresRepository
import com.example.spotifyclone.data.repositories.genresrepository.spotifycloneGenresRepository
import com.example.spotifyclone.data.repositories.homefeedrepository.HomeFeedRepository
import com.example.spotifyclone.data.repositories.homefeedrepository.spotifycloneHomeFeedRepository
import com.example.spotifyclone.data.repositories.podcastsrepository.spotifyclonePodcastsRepository
import com.example.spotifyclone.data.repositories.podcastsrepository.PodcastsRepository
import com.example.spotifyclone.data.repositories.searchrepository.spotifycloneSearchRepository
import com.example.spotifyclone.data.repositories.searchrepository.SearchRepository
import com.example.spotifyclone.data.repositories.tracksrepository.spotifycloneTracksRepository
import com.example.spotifyclone.data.repositories.tracksrepository.TracksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MusicRepositoriesModule {
    @Binds
    abstract fun bindTracksRepository(impl: spotifycloneTracksRepository): TracksRepository

    @Binds
    abstract fun bindAlbumsRepository(impl: spotifycloneAlbumsRepository): AlbumsRepository

    @Binds
    abstract fun bindArtistsRepository(impl: spotifycloneArtistsRepository): ArtistsRepository

    @Binds
    abstract fun bindGeneresRepository(impl: spotifycloneGenresRepository): GenresRepository

    @Binds
    abstract fun bindSearchRepository(impl: spotifycloneSearchRepository): SearchRepository

    @Binds
    abstract fun bindHomeFeedRepository(impl: spotifycloneHomeFeedRepository): HomeFeedRepository

    @Binds
    abstract fun bindPodcastsRepository(impl: spotifyclonePodcastsRepository): PodcastsRepository
}