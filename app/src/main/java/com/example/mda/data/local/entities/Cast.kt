package com.example.mda.data.local.entities

data class Cast(
    val id: Int,
    val name: String,
    val character: String,
    val profilePath: String?
) {
    
    fun getProfileImageUrl(): String? {
        return profilePath?.let { "https://image.tmdb.org/t/p/w185$it" }
    }
    
    
    fun getInitials(): String {
        return name.split(" ")
            .take(2)
            .joinToString("") { it.firstOrNull()?.uppercase() ?: "" }
    }
}
