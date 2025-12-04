package com.example.mda.localization

import androidx.compose.runtime.Composable
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Format utilities that respect the current app language
 */
object LocalizationFormatters {
    
    /**
     * Get locale for current language
     */
    @Composable
    fun getCurrentLocale(): Locale {
        return when (LocalAppLanguage.current) {
            LocalizationManager.Language.ENGLISH -> Locale.ENGLISH
            LocalizationManager.Language.ARABIC -> Locale("ar")
            LocalizationManager.Language.GERMAN -> Locale.GERMAN
        }
    }
    
    /**
     * Format number according to current language
     */
    @Composable
    fun formatNumber(number: Number): String {
        val locale = getCurrentLocale()
        return NumberFormat.getNumberInstance(locale).format(number)
    }
    
    /**
     * Format currency according to current language
     */
    @Composable
    fun formatCurrency(amount: Double, currencyCode: String = "USD"): String {
        val locale = getCurrentLocale()
        val formatter = NumberFormat.getCurrencyInstance(locale)
        formatter.currency = Currency.getInstance(currencyCode)
        return formatter.format(amount)
    }
    
    /**
     * Format date according to current language
     */
    @Composable
    fun formatDate(date: Date, pattern: String = "MMM dd, yyyy"): String {
        val locale = getCurrentLocale()
        val formatter = SimpleDateFormat(pattern, locale)
        return formatter.format(date)
    }
    
    /**
     * Format date from timestamp
     */
    @Composable
    fun formatDate(timestamp: Long, pattern: String = "MMM dd, yyyy"): String {
        return formatDate(Date(timestamp), pattern)
    }
    
    /**
     * Format percentage
     */
    @Composable
    fun formatPercent(value: Double): String {
        val locale = getCurrentLocale()
        val formatter = NumberFormat.getPercentInstance(locale)
        return formatter.format(value)
    }
    
    /**
     * Get text direction for current language
     */
    @Composable
    fun isRTL(): Boolean {
        return LocalAppLanguage.current == LocalizationManager.Language.ARABIC
    }
}

// Convenience composable functions
@Composable
fun localizedNumber(number: Number): String {
    return LocalizationFormatters.formatNumber(number)
}

@Composable
fun localizedCurrency(amount: Double, currencyCode: String = "USD"): String {
    return LocalizationFormatters.formatCurrency(amount, currencyCode)
}

@Composable
fun localizedDate(date: Date, pattern: String = "MMM dd, yyyy"): String {
    return LocalizationFormatters.formatDate(date, pattern)
}

@Composable
fun localizedDate(timestamp: Long, pattern: String = "MMM dd, yyyy"): String {
    return LocalizationFormatters.formatDate(timestamp, pattern)
}

@Composable
fun localizedPercent(value: Double): String {
    return LocalizationFormatters.formatPercent(value)
}
