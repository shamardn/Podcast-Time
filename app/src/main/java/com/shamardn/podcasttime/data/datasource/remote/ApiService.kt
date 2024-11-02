package com.shamardn.podcasttime.data.datasource.remote

import com.shamardn.podcasttime.data.datasource.remote.dtos.EpisodeDTO
import com.shamardn.podcasttime.data.datasource.remote.dtos.PodcastDTO
import com.shamardn.podcasttime.data.datasource.remote.dtos.PodcastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/search?media=podcast&country=eg&limit=50&country=eg&limit=50")
    suspend fun getPodcasts(
        @Query("term") term: String,
    ): PodcastResponse<PodcastDTO>

    @GET("/lookup?entity=podcastEpisode&limit=50")
    suspend fun getEpisodesById(
        @Query("id") trackId: Long,
    ): PodcastResponse<EpisodeDTO>

}