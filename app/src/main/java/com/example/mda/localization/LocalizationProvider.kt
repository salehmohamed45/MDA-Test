package com.example.mda.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

// CompositionLocal for LocalizationManager
val LocalLocalizationManager = compositionLocalOf<LocalizationManager> {
    error("LocalizationManager not provided")
}

// CompositionLocal for current language
val LocalAppLanguage = compositionLocalOf<LocalizationManager.Language> {
    LocalizationManager.Language.ENGLISH
}

/**
 * Provider for localization that ensures the entire app recomposes when language changes
 */
@Composable
fun LocalizationProvider(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val manager = remember { LocalizationManager(context) }
    val currentLanguage = manager.currentLanguage.collectAsState(initial = LocalizationManager.Language.ENGLISH).value
    
    CompositionLocalProvider(
        LocalLocalizationManager provides manager,
        LocalAppLanguage provides currentLanguage
    ) {
        content()
    }
}

/**
 * Get localized string with automatic recomposition when language changes
 */
@Composable
fun localizedString(key: String): String {
    val manager = LocalLocalizationManager.current
    val language = LocalAppLanguage.current
    return manager.getString(key, language)
}

@Composable
fun localizedString(key: String, replacements: Map<String, String>): String {
    val manager = LocalLocalizationManager.current
    val language = LocalAppLanguage.current
    var text = manager.getString(key, language)
    
    if (replacements.isNotEmpty()) {
        val builder = StringBuilder(text)
        replacements.forEach { (placeholder, value) ->
            val pattern = "{$placeholder}"
            var index = builder.indexOf(pattern)
            while (index >= 0) {
                builder.replace(index, index + pattern.length, value)
                index = builder.indexOf(pattern, index + value.length)
            }
        }
        text = builder.toString()
    }
    
    return text
}

@Composable
fun localizedString(key: String, placeholder: String, value: String): String {
    return localizedString(key, mapOf(placeholder to value))
}
