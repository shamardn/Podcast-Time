package com.shamardn.podcasttime.data.remote

import com.shamardn.podcasttime.domain.entity.EpisodeDTO
import com.shamardn.podcasttime.domain.entity.PodcastDTO
import com.shamardn.podcasttime.domain.entity.PodcastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/search?media=podcast&country=eg")
    suspend fun getPodcasts(
        @Query("term") term: String,
    ): PodcastResponse<PodcastDTO>

    @GET("/lookup?entity=podcastEpisode")
    suspend fun getPodcastById(
        @Query("id") trackId: Int,
    ): PodcastResponse<EpisodeDTO>
}