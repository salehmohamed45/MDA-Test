package com.example.mda.ui.screens.home.homeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mda.data.remote.model.Movie
import com.example.mda.ui.screens.favorites.FavoritesViewModel
import com.example.mda.ui.screens.favorites.components.FavoriteButton

@Composable
fun ExampleSectionWithFavorites(
    movies: List<Movie>,
    navController: NavController,
    favoritesViewModel: FavoritesViewModel,
    isAuthenticated: Boolean
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(movies) { movie ->
            MovieCardWithFavorite(
                movie = movie,
                onClick = {
                    val type = movie.mediaType ?: "movie"
                    navController.navigate("detail/$type/${movie.id}")
                },
                favoriteButton = {
                    FavoriteButton(
                        movie = movie,
                        viewModel = favoritesViewModel,
                        showBackground = true,
                        isAuthenticated = isAuthenticated,
                        onLoginRequired = { navController.navigate("settings") }
                    )
                }
            )
        }
    }
}

@Composable
fun ExampleGridWithFavorites(
    movies: List<Movie>,
    navController: NavController,
    favoritesViewModel: FavoritesViewModel,
    isAuthenticated: Boolean
) {
    androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
        columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(movies.size) { index ->
            val movie = movies[index]
            MovieCardWithFavorite(
                movie = movie,
                onClick = {
                    val type = movie.mediaType ?: "movie"
                    navController.navigate("detail/$type/${movie.id}")
                },
                favoriteButton = {
                    FavoriteButton(
                        movie = movie,
                        viewModel = favoritesViewModel,
                        showBackground = true,
                        isAuthenticated = isAuthenticated,
                        onLoginRequired = { navController.navigate("settings") }
                    )
                }
            )
        }
    }
}

