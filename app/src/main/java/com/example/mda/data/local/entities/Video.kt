package com.example.mda.data.local.entities

data class Video(
    val key: String,
    val name: String,
    val site: String,
    val type: String
) {
    
    fun getYouTubeUrl(): String {
        return "https:
    }
    
    
    fun getThumbnailUrl(): String {
        return "https:
    }
    
    
    fun isYouTube(): Boolean {
        return site.equals("YouTube", ignoreCase = true)
    }
    
    
    fun isTrailer(): Boolean {
        return type.equals("Trailer", ignoreCase = true)
    }
}
