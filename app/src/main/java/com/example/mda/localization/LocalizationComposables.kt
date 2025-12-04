package com.example.mda.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun localizedString(key: String): String {
    return try {
        val manager = LocalLocalizationManager.current
        val language = LocalAppLanguage.current
        manager.getString(key, language)
    } catch (e: IllegalStateException) {
        val context = LocalContext.current
        val manager = remember { LocalizationManager(context) }
        val language = manager.currentLanguage.collectAsState(initial = LocalizationManager.Language.ENGLISH).value
        manager.getString(key, language)
    }
}

@Composable
fun localizedString(key: String, replacements: Map<String, String>): String {
    var text = localizedString(key)
    replacements.forEach { (placeholder, value) ->
        text = text.replace("{$placeholder}", value)
    }
    return text
}

@Composable
fun localizedString(key: String, placeholder: String, value: String): String {
    return localizedString(key, mapOf(placeholder to value))
}
