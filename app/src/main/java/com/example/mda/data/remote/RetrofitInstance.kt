package com.example.mda.data.remote

import com.example.mda.data.remote.api.TmdbApi
import com.example.mda.localization.LanguageProvider
import com.example.mda.util.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.HttpUrl
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(AuthInterceptor())
        .addInterceptor(LanguageQueryInterceptor())
        .build()

    val api: TmdbApi by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbApi::class.java)
    }

    private class LanguageQueryInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val originalUrl = original.url
            if (originalUrl.queryParameter("language") != null) {
                return chain.proceed(original)
            }
            val path = originalUrl.encodedPath
            val isPeopleEndpoint =
                path.startsWith("/person") || path.startsWith("/search/person") || path.contains("/person/") || path == "/person/popular"
            if (isPeopleEndpoint) {
                return chain.proceed(original)
            }
            val code = when (LanguageProvider.currentCode) {
                "ar" -> "ar"
                "de" -> "de"
                else -> "en"
            }
            val newUrl = originalUrl.newBuilder()
                .addQueryParameter("language", code)
                .build()
            val newRequest = original.newBuilder()
                .url(newUrl)
                .build()
            return chain.proceed(newRequest)
        }
    }
}
