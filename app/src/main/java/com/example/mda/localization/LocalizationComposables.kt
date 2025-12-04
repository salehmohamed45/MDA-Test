package com.example.mda.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext

@Composable
fun localizedString(key: String): String {
    val context = LocalContext.current
    val manager = LocalizationManager(context)
    val language = manager.currentLanguage.collectAsState(initial = LocalizationManager.Language.ENGLISH).value
    return manager.getString(key, language)
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
