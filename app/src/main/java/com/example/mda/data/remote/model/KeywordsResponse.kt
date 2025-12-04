package com.example.mda.data.remote.model

// Data model

import com.google.gson.annotations.SerializedName

data class KeywordsResponse(
    @SerializedName("keywords") val keywords: List<KeywordItem>? = null,
    @SerializedName("results") val results: List<KeywordItem>? = null
) {
    fun all(): List<KeywordItem> = keywords ?: results ?: emptyList()
}

data class KeywordItem(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
