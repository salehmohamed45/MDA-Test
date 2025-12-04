package com.example.mda.ui.navigation

// Navigation configuration

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

data class TopBarState(
    val title: String = "",
    val subtitle: String? = null,
    val showBackButton: Boolean = false,
    val actions: @Composable RowScope.() -> Unit = {}
)
