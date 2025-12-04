package com.example.mda.data.repository

// Repository for data operations

import com.example.mda.data.datastore.SessionManager
import com.example.mda.data.remote.api.TmdbApi
import com.example.mda.data.remote.model.auth.AccountDetails
import com.example.mda.data.remote.model.auth.SessionRequest
import com.example.mda.data.remote.model.auth.TokenResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AuthRepository(
    private val api: TmdbApi,
    private val sessionManager: SessionManager
) {

    
    suspend fun createRequestToken(): Result<TokenResponse> {
        return try {
            val response = api.createRequestToken()
            if (response.isSuccessful && response.body() != null) {
                val tokenResponse = response.body()!!
                sessionManager.saveRequestToken(tokenResponse.requestToken)
                Result.success(tokenResponse)
            } else {
                Result.failure(Exception("Failed to create request token: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    
    fun getAuthUrl(requestToken: String): String {
        return "https://www.themoviedb.org/authenticate/$requestToken"
    }

    
    suspend fun createSession(requestToken: String): Result<String> {
        return try {
            val response = api.createSession(SessionRequest(requestToken))
            if (response.isSuccessful && response.body() != null) {
                val sessionId = response.body()!!.sessionId
                sessionManager.saveSessionId(sessionId)
                Result.success(sessionId)
            } else {
                Result.failure(Exception("Failed to create session: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    
    suspend fun getAccountDetails(): Result<AccountDetails> {
        return try {
            val sessionId = sessionManager.sessionId.first()

            if (sessionId.isNullOrEmpty()) {
                return Result.failure(Exception("No session ID found. Please login first."))
            }

            val response = api.getAccountDetails(sessionId)

            if (response.isSuccessful && response.body() != null) {
                val details = response.body()!!

                sessionManager.saveAccountId(details.id)
                sessionManager.saveAccountInfo(details.name, details.username)

                return Result.success(details)

            } else {
                Result.failure(Exception("Failed to get account details: ${response.message()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    
    fun isLoggedIn(): Flow<Boolean> {
        return sessionManager.sessionId.map { it != null }
    }

    
    fun getSessionId(): Flow<String?> {
        return sessionManager.sessionId
    }

    
    fun getRequestToken(): Flow<String?> {
        return sessionManager.requestToken
    }

    
    fun getAccountId(): Flow<Int?> {
        return sessionManager.accountId
    }

    
    suspend fun logout() {
        sessionManager.clearSession()
    }
}
