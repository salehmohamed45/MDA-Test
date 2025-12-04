package com.example.mda.viewmodel

// ViewModel for managing UI state

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.example.mda.data.local.entities.MediaEntity
import com.example.mda.data.repository.MoviesRepository
import com.example.mda.filteration.FilterType
import com.example.mda.ui.screens.genreDetails.GenreDetailsViewModel
import com.example.mda.ui.screens.genreDetails.MediaTypeFilter
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalCoroutinesApi::class)
class GenreDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: MoviesRepository
    private lateinit var viewModel: GenreDetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun sampleMedia(id: Int, adult: Boolean = false, voteAvg: Double = 5.0) =
        MediaEntity(
            id = id,
            title = "Movie $id",
            name = null,
            overview = "Overview",
            posterPath = "/p$id.jpg",
            backdropPath = null,
            releaseDate = "2020-01-0$id",
            firstAirDate = null,
            voteAverage = voteAvg,
            mediaType = "movie",
            adult = adult,
            voteCount = 100L,
            genreIds = listOf(1)
        )

    @Test
    fun `loadMoviesByGenre adds movies and stops when empty`() = runTest {
        coEvery { repository.getMoviesByGenre(1, 1) } returns listOf(sampleMedia(1), sampleMedia(2))
        coEvery { repository.getMoviesByGenre(1, 2) } returns emptyList()

        viewModel = GenreDetailsViewModel(repository)

        viewModel.loadMoviesByGenre(1)
        advanceUntilIdle()

        assertEquals(2, viewModel.movies.size)
        assertFalse(viewModel.isLoading)
        viewModel.loadMoviesByGenre(1)
        advanceUntilIdle()
        assertEquals(2, viewModel.movies.size)
    }

    @Test
    fun `applyFilter family friendly filters out adult`() = runTest {
        coEvery { repository.getMoviesByGenre(1, 1) } returns listOf(sampleMedia(1, adult = true), sampleMedia(2, adult = false))
        viewModel = GenreDetailsViewModel(repository)

        viewModel.loadMoviesByGenre(1)
        advanceUntilIdle()
        assertEquals(2, viewModel.movies.size)

        viewModel.applyFilter(FilterType.FAMILY_FRIENDLY)
        assertEquals(1, viewModel.movies.size)
        assertFalse(viewModel.movies.any { it.adult == true })
    }

    @Test
    fun `setMediaTypeFilter resets and loads`() = runTest {
        coEvery { repository.getMoviesByGenre(1, any()) } returns listOf(sampleMedia(1))
        coEvery { repository.getTvShowsByGenre(1, any()) } returns listOf(sampleMedia(10))

        viewModel = GenreDetailsViewModel(repository)
        advanceUntilIdle()

        viewModel.setMediaTypeFilter(MediaTypeFilter.TV_SHOWS, 1)
        advanceUntilIdle()

        assertTrue(viewModel.movies.any { it.id == 10 })
    }
}
