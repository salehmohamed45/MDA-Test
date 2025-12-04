package com.example.mda.data.repository

// Repository for data operations

import com.example.mda.data.local.dao.MediaDao
import com.example.mda.data.local.entities.MediaEntity
import com.example.mda.data.remote.api.TmdbApi
import com.example.mda.data.remote.model.Movie
import com.example.mda.data.repository.mappers.toMediaEntity
import kotlinx.coroutines.flow.first

class MoviesByGenreRepository(
    private val api: TmdbApi,
    private val dao: MediaDao
) {

    suspend fun getMoviesByGenre(genreId: Int): List<MediaEntity> {
        val cached: List<MediaEntity> = dao.getAll().first()
            .filter { it.genreIds?.contains(genreId) == true }

        if (cached.isNotEmpty()) return cached

        val response = api.getMoviesByGenre(genreId)
        if (response.isSuccessful) {
            val movies: List<MediaEntity> = response.body()?.results
                ?.map { it.toMediaEntity("movie") } ?: emptyList()

            movies.forEach { dao.upsert(it) }

            return movies
        } else throw Exception("Failed to load movies by genre")
    }

}
