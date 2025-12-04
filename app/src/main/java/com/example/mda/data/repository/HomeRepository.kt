package com.example.mda.data.repository

// Repository for data operations

import com.example.mda.data.remote.model.Movie

interface MovieRepository {
    suspend fun getBannerMovies(): List<Movie>
    suspend fun getTrendingMovies(): List<Movie>
}