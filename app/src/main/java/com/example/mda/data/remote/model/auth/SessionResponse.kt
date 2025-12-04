package com.example.mda.data.remote.model.auth

// Data model

import com.google.gson.annotations.SerializedName

data class SessionResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("session_id")
    val sessionId: String
)

data class SessionRequest(
    @SerializedName("request_token")
    val requestToken: String
)
