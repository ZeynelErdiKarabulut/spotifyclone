package com.example.spotifyclone.data.repositories.genresrepository

import com.example.spotifyclone.data.remote.musicservice.SupportedSpotifyGenres
import com.example.spotifyclone.data.remote.musicservice.toGenre
import com.example.spotifyclone.domain.Genre
import javax.inject.Inject

class spotifycloneGenresRepository @Inject constructor() : GenresRepository {
    override fun fetchAvailableGenres(): List<Genre> = SupportedSpotifyGenres.values().map {
        it.toGenre()
    }
}