

package com.example.mda.data.remote.api

import com.example.mda.BuildConfig
import com.example.mda.data.remote.model.*
import com.example.mda.data.remote.model.auth.*
import retrofit2.Response
import retrofit2.http.*

interface TmdbApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String? = null,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language") language: String? = null,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieResponse>

    @GET("discover/tv")
    suspend fun getTvShowsByGenre(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String? = null,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieResponse>

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("language") language: String? = null,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("append_to_response") appendToResponse: String = "videos,credits",
        @Query("include_image_language") includeImageLanguage: String = "en,null",
        @Query("language") language: String? = null,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieDetailsResponse>

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String? = null,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieResponse>

    @GET("movie/{movie_id}/recommendations")
    suspend fun getRecommendedMovies(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String? = null,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieResponse>

    @GET("movie/{movie_id}/release_dates")
    suspend fun getMovieReleaseDates(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<ReleaseDatesResponse>

    @GET("movie/{movie_id}/watch/providers")
    suspend fun getMovieWatchProviders(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<WatchProvidersResponse>

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String? = null,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<ReviewsResponse>

    @GET("movie/{movie_id}/keywords")
    suspend fun getMovieKeywords(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<KeywordsResponse>

    @GET("tv/popular")
    suspend fun getPopularTvShows(
        @Query("language") language: String? = null,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieResponse>

    @GET("tv/{tv_id}")
    suspend fun getTvDetails(
        @Path("tv_id") tvId: Int,
        @Query("append_to_response") appendToResponse: String = "videos,credits",
        @Query("include_image_language") includeImageLanguage: String = "en,null",
        @Query("language") language: String? = null,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieDetailsResponse>

    @GET("tv/{tv_id}/similar")
    suspend fun getSimilarTvShows(
        @Path("tv_id") tvId: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String? = null,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieResponse>

    @GET("tv/{tv_id}/recommendations")
    suspend fun getRecommendedTvShows(
        @Path("tv_id") tvId: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String? = null,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieResponse>

    @GET("tv/{tv_id}/watch/providers")
    suspend fun getTvWatchProviders(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<WatchProvidersResponse>

    @GET("tv/{tv_id}/reviews")
    suspend fun getTvReviews(
        @Path("tv_id") tvId: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String? = null,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<ReviewsResponse>

    @GET("tv/{tv_id}/keywords")
    suspend fun getTvKeywords(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<KeywordsResponse>

    @GET("trending/{media_type}/{time_window}")
    suspend fun getTrendingMedia(
        @Path("media_type") mediaType: String = "all",
        @Path("time_window") timeWindow: String = "day",
        @Query("language") language: String? = null,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieResponse>

    @GET("person/popular")
    suspend fun getPopularPeople(
        @Query("page") page: Int = 1,
        @Query("language") language: String? = null,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
    ): Response<ActorResponse>

    @GET("search/person")
    suspend fun searchPeople(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String? = null,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
    ): Response<ActorResponse>

    @GET("person/{person_id}")
    suspend fun getActorDetails(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("append_to_response") appendToResponse: String
    ): Response<ActorFullDetails>

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("language") language: String? = null,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<GenreResponse>

    @GET("search/multi")
    suspend fun searchMulti(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String? = null,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieResponse>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String? = null,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieResponse>

    @GET("search/tv")
    suspend fun searchTvShows(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String? = null,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieResponse>

    @GET("authentication/token/new")
    suspend fun createRequestToken(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<TokenResponse>

    @POST("authentication/session/new")
    suspend fun createSession(
        @Body request: SessionRequest,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<SessionResponse>

    @GET("account")
    suspend fun getAccountDetails(
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<AccountDetails>

    @GET("movie/{movie_id}/recommendations")
    suspend fun getMovieRecommendations(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String? = null,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieResponse>

    @GET("tv/{tv_id}/recommendations")
    suspend fun getTvRecommendations(
        @Path("tv_id") tvId: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String? = null,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieResponse>

    @GET("account/{account_id}/rated/movies")
    suspend fun getRatedMovies(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieResponse>

    @GET("account/{account_id}/rated/tv")
    suspend fun getRatedTvShows(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieResponse>

    @POST("account/{account_id}/favorite")
    suspend fun markFavorite(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Body body: Map<String, @JvmSuppressWildcards Any>,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<Unit>

    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavoriteMovies(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<FavoriteMoviesResponse>

}

