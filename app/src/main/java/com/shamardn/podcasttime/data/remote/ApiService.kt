package com.shamardn.podcasttime.data.remote

import com.shamardn.podcasttime.domain.entity.Episode
import com.shamardn.podcasttime.domain.entity.Podcast
import com.shamardn.podcasttime.domain.entity.PodcastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/search?media=podcast&country=eg")
    suspend fun getPodcasts(
        @Query("term") term: String,
    ): PodcastResponse<Podcast>

    @GET("/lookup?entity=podcastEpisode")
    suspend fun getPodcastById(
        @Query("id") trackId: Int,
    ): PodcastResponse<Episode>
}