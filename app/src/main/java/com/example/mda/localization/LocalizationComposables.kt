package com.example.mda.localization

import androidx.compose.runtime.Composable

@Composable
fun localizedString(key: String): String {
    val manager = LocalLocalizationManager.current
    val language = LocalAppLanguage.current
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
