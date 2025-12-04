package com.example.mda.data.remote.model

// Data model

import com.google.gson.annotations.SerializedName

data class WatchProvidersResponse(
    @SerializedName("results") val results: Map<String, WatchProviderCountry> = emptyMap()
)

data class WatchProviderCountry(
    @SerializedName("link") val link: String? = null,
    @SerializedName("flatrate") val flatrate: List<WatchProviderItem>? = null,
    @SerializedName("buy") val buy: List<WatchProviderItem>? = null,
    @SerializedName("rent") val rent: List<WatchProviderItem>? = null
)

data class WatchProviderItem(
    @SerializedName("provider_id") val providerId: Int,
    @SerializedName("provider_name") val providerName: String,
    @SerializedName("logo_path") val logoPath: String?
)
