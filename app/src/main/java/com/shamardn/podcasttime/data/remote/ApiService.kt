package com.shamardn.podcasttime.data.remote

import com.shamardn.podcasttime.domain.entity.PodcastResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


// https://itunes.apple.com/search?term=power+english&country=eg&media=podcast

interface ApiService {
    @GET("/search?media=podcast&country=eg")
    suspend fun getPodcasts(
        @Query("term") term: String,
    ): PodcastResponse

    companion object {
        val instance: ApiService by lazy {
             val okHttp = OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://itunes.apple.com")
                .client(okHttp)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(ApiService::class.java)
        }
    }
}