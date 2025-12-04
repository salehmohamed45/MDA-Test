package com.example.mda.domain.usecase

import com.example.mda.data.local.entities.MediaEntity
import com.example.mda.data.repository.MoviesRepository

class SearchMoviesUseCase(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(query: String, filter: String): List<MediaEntity> {
        val apiResults = repository.searchByType(query.trim(), filter.lowercase())

        if (apiResults.isEmpty()) {
            val trending = repository.getTrendingMedia()
            return trending.filter {
                val title = (it.title ?: it.name ?: "").lowercase()
                title.contains(query.lowercase())
            }
        }

        return apiResults
    }
}