package org.myf.demo.core.network.countriesRetrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.myf.demo.core.network.BuildConfig
import retrofit2.Retrofit

object CountriesApiClient {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun create(): CountriesService =
        Retrofit.Builder()
            .baseUrl(BuildConfig.COUNTRIES_BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    )
                    .build()
            )
            .addConverterFactory(
                @OptIn(ExperimentalSerializationApi::class)
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(CountriesService::class.java)

}