package com.example.mda.data.repository.mappers

import com.example.mda.data.local.entities.Cast
import com.example.mda.data.local.entities.MediaEntity
import com.example.mda.data.local.entities.MoviesViewedEntitty
import com.example.mda.data.local.entities.Video
import com.example.mda.data.remote.model.Movie
import com.example.mda.data.remote.model.MovieDetailsResponse

fun Movie.toMediaEntity(defaultType: String? = this.mediaType): MediaEntity {
    val realType = this.mediaType ?: defaultType ?: if (!this.name.isNullOrEmpty() && this.title.isNullOrEmpty()) "tv" else "movie"

    return MediaEntity(
        id = this.id,
        title = this.title ?: "",
        name = this.name ?: this.title ?: "",
        overview = this.overview ?: "",
        posterPath = this.posterPath,
        backdropPath = this.backdropPath,
        voteAverage = this.voteAverage ?: 0.0,
        releaseDate = this.releaseDate ?: "",
        firstAirDate = this.firstAirDate ?: "",
        mediaType = realType,
        adult = this.adult ?: false,
        genreIds = this.genreIds ?: emptyList(),
        genres = emptyList()
    )
}

fun MovieDetailsResponse.toMediaEntity(type: String = "movie"): MediaEntity {
    val genreNames = this.genres?.mapNotNull { it.name } ?: emptyList()
    val genreIds = this.genres?.map { it.id } ?: emptyList()
    
    val castList = this.credits?.cast?.take(20)?.map { castItem ->
        Cast(
            id = castItem.id,
            name = castItem.name ?: "Unknown",
            character = castItem.character ?: "",
            profilePath = castItem.profilePath
        )
    }
    
    val videosList = this.videos?.results?.filter { 
        it.site?.equals("YouTube", ignoreCase = true) == true 
    }?.take(10)?.map { videoItem ->
        Video(
            key = videoItem.key ?: "",
            name = videoItem.name ?: "Video",
            site = videoItem.site ?: "YouTube",
            type = videoItem.type ?: "Clip"
        )
    }
    
    val languages = this.spokenLanguages?.mapNotNull { it.name }
    
    val companies = this.productionCompanies?.mapNotNull { it.name }
    
    val countries = this.productionCountries?.mapNotNull { it.name }

    val isTv = type == "tv"
    val mappedTitle = if (isTv) (this.name ?: this.title) else (this.title ?: this.name)
    val mappedRelease = if (isTv) null else this.releaseDate
    val mappedFirstAir = if (isTv) (this.firstAirDate) else null
    val mappedRuntime = if (isTv) this.episodeRunTime?.firstOrNull() else this.runtime

    val posters = this.images?.posters?.mapNotNull { it.filePath }
    val backdrops = this.images?.backdrops?.mapNotNull { it.filePath }

    return MediaEntity(
        id = this.id,
        title = if (isTv) mappedTitle else (this.title ?: mappedTitle ?: ""),
        name = if (isTv) (mappedTitle ?: "") else (this.title ?: mappedTitle ?: ""),
        overview = this.overview ?: "",
        posterPath = this.posterPath,
        backdropPath = this.backdropPath,
        voteAverage = this.voteAverage ?: 0.0,
        releaseDate = mappedRelease ?: "",
        firstAirDate = mappedFirstAir,
        mediaType = type,
        adult = this.adult ?: false,
        genreIds = genreIds,
        genres = genreNames,
        
        runtime = mappedRuntime,
        tagline = this.tagline,
        status = this.status,
        voteCount = this.voteCount,
        budget = this.budget,
        revenue = this.revenue,
        imdbId = this.imdbId,
        homepage = this.homepage,
        spokenLanguages = languages,
        productionCompanies = companies,
        productionCountries = countries,
        cast = castList,
        videos = videosList,
        posters = posters,
        backdrops = backdrops
    )
}

fun MoviesViewedEntitty.toMediaEntity(): MediaEntity {
    return MediaEntity(
        id = this.id,
        title = if (this.mediaType == "movie") this.name else null,
        name = if (this.mediaType == "tv") this.name else null,
        posterPath = this.posterPath,
        backdropPath = this.backdropPath,
        mediaType = this.mediaType ?: "movie",
        overview = "Recently Viewed",
        voteAverage = 0.0,
        releaseDate = null,
        firstAirDate = null,
        timestamp = System.currentTimeMillis()
    )
}
