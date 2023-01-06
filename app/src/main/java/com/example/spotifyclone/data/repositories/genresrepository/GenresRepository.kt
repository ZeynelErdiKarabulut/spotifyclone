package com.example.spotifyclone.data.repositories.genresrepository

import com.example.spotifyclone.domain.Genre

/**
 * A repository that contains all methods related to genres.
 */
interface GenresRepository {
    fun fetchAvailableGenres(): List<Genre>
}