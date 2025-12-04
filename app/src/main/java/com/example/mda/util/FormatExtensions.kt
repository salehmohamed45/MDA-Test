package com.example.mda.util

import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

fun Int?.toRuntimeString(): String {
    if (this == null || this == 0) return "N/A"
    
    val hours = this / 60
    val minutes = this % 60
    
    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        else -> "${minutes}m"
    }
}

fun Long?.toFormattedMoney(): String {
    if (this == null || this == 0L) return "N/A"
    
    val absValue = abs(this.toDouble())
    
    return when {
        absValue >= 1_000_000_000 -> {
            val billions = absValue / 1_000_000_000
            "$${String.format(Locale.US, "%.1f", billions)}B"
        }
        absValue >= 1_000_000 -> {
            val millions = absValue / 1_000_000
            "$${String.format(Locale.US, "%.1f", millions)}M"
        }
        absValue >= 1_000 -> {
            val thousands = absValue / 1_000
            "$${String.format(Locale.US, "%.1f", thousands)}K"
        }
        else -> {
            "$$this"
        }
    }
}

fun Long?.toFormattedNumber(): String {
    if (this == null || this == 0L) return "N/A"
    return NumberFormat.getNumberInstance(Locale.US).format(this)
}

fun Double?.toPercentage(): String {
    if (this == null || this == 0.0) return "N/A"
    val percentage = (this * 10).toInt()
    return "$percentage%"
}

fun Double?.toRatingString(): String {
    if (this == null || this == 0.0) return "N/A"
    return String.format(Locale.US, "%.1f", this)
}

fun String?.toYear(): String {
    if (this.isNullOrEmpty()) return "N/A"
    return this.take(4)
}

fun List<String>?.toCommaSeparated(): String {
    if (this.isNullOrEmpty()) return "N/A"
    return this.joinToString(", ")
}

fun String?.truncate(maxLength: Int = 150): String {
    if (this.isNullOrEmpty()) return "N/A"
    return if (this.length > maxLength) {
        "${this.take(maxLength)}..."
    } else {
        this
    }
}
