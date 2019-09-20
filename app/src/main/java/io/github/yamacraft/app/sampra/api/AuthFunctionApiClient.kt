package io.github.yamacraft.app.sampra.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import io.github.yamacraft.app.sampra.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AuthFunctionApiClient {
    private const val BASE_URL = BuildConfig.FUNCTIONS_BASE_URL
    private const val TIMEOUT_MINUTES = 1L

    fun create(): AuthFunctionApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(createOkHttpClient())
        .addConverterFactory(createGsonConverterFactory())
        .build()
        .create(AuthFunctionApi::class.java)

    private fun createOkHttpClient() = OkHttpClient().newBuilder()
        .connectTimeout(TIMEOUT_MINUTES, TimeUnit.MINUTES)
        .readTimeout(TIMEOUT_MINUTES, TimeUnit.MINUTES)
        .addInterceptor(createHttpLoggingInterceptor())
        .build()

    private fun createHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.HEADERS)

    private fun createGsonConverterFactory() = GsonConverterFactory.create(
        GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    )
}

