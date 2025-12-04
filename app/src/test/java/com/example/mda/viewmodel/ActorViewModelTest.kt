package com.example.mda.viewmodel

// ViewModel for managing UI state

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mda.data.repository.ActorsRepository
import com.example.mda.ui.screens.actors.ActorUiState
import com.example.mda.ui.screens.actors.ActorViewModel
import com.example.mda.ui.screens.actors.ViewType
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ActorViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ActorViewModel
    private val repository = mockk<ActorsRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Log::class)
        every { Log.d(any<String>(), any<String>()) } returns 0
        every { Log.i(any<String>(), any<String>()) } returns 0
        every { Log.e(any<String>(), any<String>()) } returns 0
        every { Log.w(any<String>(), any<String>()) } returns 0
        coEvery { repository.getPopularActorsWithCache(page = 1) } returns listOf(
            com.example.mda.data.local.entities.ActorEntity(
                id = 1,
                name = "Tom Cruise",
                profilePath = null,
                biography = "An actor",
                birthday = "1962-07-03",
                placeOfBirth = "USA",
                knownForDepartment = "Acting",
                knownFor = "[]"
            )
        )

        viewModel = ActorViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should eventually become Success when load succeeds`() = runTest(testDispatcher) {
        val state = viewModel.state.value
        assertTrue(state is ActorUiState.Success)

        val successState = state as ActorUiState.Success
        assertEquals(1, successState.actors.size)
        assertEquals("Tom Cruise", successState.actors.first().name)
    }

    @Test
    fun `toggleViewType should switch between GRID and LIST`() = runTest(testDispatcher) {
        val initialType = viewModel.viewType.value
        viewModel.toggleViewType()
        val toggledType = viewModel.viewType.value

        assertNotEquals(initialType, toggledType)
        assertTrue(toggledType == ViewType.LIST || toggledType == ViewType.GRID)
    }
}