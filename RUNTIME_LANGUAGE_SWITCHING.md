# Runtime Language Switching Guide

## Overview

This guide explains how runtime language switching works in the MDA Movie App, allowing users to change the app language instantly without restart while preserving all app state.

## Architecture

### Core Components

1. **LocalizationProvider** (`LocalizationProvider.kt`)
   - Wraps entire app in MainActivity
   - Provides language state via CompositionLocal
   - Triggers recomposition when language changes

2. **LocalizationManager** (`LocalizationManager.kt`)
   - Manages language persistence via DataStore
   - Provides translation strings for EN, AR, DE
   - Thread-safe language storage

3. **LocalizationComposables** (`LocalizationComposables.kt`)
   - `localizedString()` function for getting translations
   - Automatically recomposes when language changes
   - Supports placeholder replacements

4. **LanguageProvider** (`LanguageProvider.kt`)
   - Static language code for API calls
   - Updated synchronously with UI language

## How It Works

### 1. App Initialization

```kotlin
// MainActivity.kt
MovieAppTheme(darkTheme = darkTheme) {
    LocalizationProvider {  // ← Wraps entire app
        val appLanguage = LocalAppLanguage.current
        
        // RTL/LTR support
        val layoutDir = if (appLanguage == Language.ARABIC) 
            LayoutDirection.Rtl 
        else 
            LayoutDirection.Ltr
        
        CompositionLocalProvider(LocalLayoutDirection provides layoutDir) {
            // App content here
        }
    }
}
```

### 2. Language Change Flow

When user selects a language in Settings:

```
User clicks language
    ↓
LanguageSettingsScreen calls manager.setLanguage(language)
    ↓
LocalizationManager writes to DataStore
    ↓
DataStore Flow emits new language
    ↓
LocalizationProvider observes Flow change
    ↓
CompositionLocal value updates
    ↓
All composables using localizedString() recompose
    ↓
Layout direction updates (RTL/LTR)
    ↓
UI fully updated - NO RESTART NEEDED
```

### 3. Using Translations

```kotlin
// Simple usage
@Composable
fun MyScreen() {
    Text(localizedString(LocalizationKeys.HOME_TITLE))
}

// With placeholders
@Composable
fun GreetingScreen(userName: String) {
    Text(localizedString(
        LocalizationKeys.WELCOME_MESSAGE,
        "name", userName
    ))
}
```

### 4. State Preservation

**Navigation State:**
- Navigation stack preserved during language change
- Current route stays active
- Back stack maintained

**Form Input:**
- Text field values preserved via `remember` states
- Form validation state maintained
- User input not lost

**ViewModels:**
- ViewModel instances preserved (tied to lifecycle)
- Data cached in ViewModels stays intact
- Only UI strings update

## Supported Languages

| Language | Code | Display Name | RTL |
|----------|------|--------------|-----|
| English  | en   | English      | No  |
| Arabic   | ar   | العربية      | Yes |
| German   | de   | Deutsch      | No  |

## RTL (Right-to-Left) Support

### Automatic RTL Switching

When Arabic is selected:
- Layout direction becomes RTL
- Text alignment flips
- Navigation icons mirror
- Padding/margins reverse
- Scrolling direction inverts

### Implementation

```kotlin
// Automatic RTL detection
val layoutDir = if (appLanguage == Language.ARABIC) 
    LayoutDirection.Rtl 
else 
    LayoutDirection.Ltr

CompositionLocalProvider(LocalLayoutDirection provides layoutDir) {
    // All composables inside automatically respect RTL
}
```

## Adding New Translations

### 1. Add Key to LocalizationKeys.kt

```kotlin
object LocalizationKeys {
    const val MY_NEW_KEY = "my_new_key"
}
```

### 2. Add Translations to LocalizationManager.kt

```kotlin
// English
object StringsEN {
    private val strings = mapOf(
        "my_new_key" to "Hello World"
    )
}

// Arabic
object StringsAR {
    private val strings = mapOf(
        "my_new_key" to "مرحبا بالعالم"
    )
}

// German
object StringsDE {
    private val strings = mapOf(
        "my_new_key" to "Hallo Welt"
    )
}
```

### 3. Use in Composable

```kotlin
@Composable
fun MyScreen() {
    Text(localizedString(LocalizationKeys.MY_NEW_KEY))
}
```

## Testing Language Switching

### Manual Testing

1. Open app
2. Navigate to Settings → Language
3. Select different language
4. Observe:
   - All visible text updates immediately
   - Layout direction changes (for Arabic)
   - Navigation state preserved
   - Form inputs retained
   - No app restart

### Automated Testing

```kotlin
@Test
fun testLanguageSwitching() {
    composeTestRule.setContent {
        LocalizationProvider {
            LanguageSettingsScreen(navController, onTopBarStateChange = {})
        }
    }
    
    // Click Arabic
    composeTestRule.onNodeWithText("العربية").performClick()
    
    // Verify RTL
    // Verify translations updated
}
```

## Performance Considerations

### Optimizations

1. **DataStore Caching**: Language preference cached in memory
2. **Lazy Recomposition**: Only visible composables recompose
3. **ViewModel Preservation**: ViewModels not recreated
4. **Navigation Efficiency**: Back stack efficiently maintained

### Best Practices

- ✅ Use `localizedString()` for all UI text
- ✅ Use `remember` for form state
- ✅ Keep translations in LocalizationManager
- ❌ Don't hardcode strings in composables
- ❌ Don't recreate ViewModels on language change

## Troubleshooting

### Language not updating?

Check:
1. LocalizationProvider wraps your composable
2. Using `localizedString()` not hardcoded strings
3. DataStore permissions granted

### RTL not working?

Check:
1. `LocalLayoutDirection` provided
2. Using RTL-aware modifiers (padding vs absolutePadding)
3. Arabic language selected

### State lost on language change?

Check:
1. Using `remember` for local state
2. ViewModel tied to proper lifecycle
3. Not recreating navigation in language change scope

## Current Features

The following features are already implemented:

- ✅ **Date/time formatting per locale** - Via LocalizationFormatters.kt
- ✅ **Number formatting per locale** - Via LocalizationFormatters.kt  
- ✅ **Currency formatting per locale** - Via LocalizationFormatters.kt
- ✅ **RTL/LTR layout support** - Automatic for Arabic
- ✅ **Three languages** - English, Arabic, German

## Future Enhancements

Potential additional improvements:
- [ ] Pluralization support (1 item vs 2 items)
- [ ] More languages (French, Spanish, Chinese, etc.)
- [ ] Language-specific fonts
- [ ] Region-specific variations (en-US vs en-GB)
- [ ] Voice-over localization support

## Resources

- [Jetpack Compose State Documentation](https://developer.android.com/jetpack/compose/state)
- [CompositionLocal Guide](https://developer.android.com/jetpack/compose/compositionlocal)
- [Android RTL Support](https://developer.android.com/training/basics/supporting-devices/languages#rtl-android)
- [Material Design Internationalization](https://material.io/design/typography/language-support.html)
