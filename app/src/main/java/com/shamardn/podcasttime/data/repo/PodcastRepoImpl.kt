package com.shamardn.podcasttime.data.repo

import com.shamardn.podcasttime.data.remote.ApiService
import com.shamardn.podcasttime.domain.entity.Episode
import com.shamardn.podcasttime.domain.entity.Podcast
import com.shamardn.podcasttime.domain.entity.PodcastResponse
import com.shamardn.podcasttime.domain.repo.PodcastRepo

class PodcastRepoImpl(private val apiService: ApiService): PodcastRepo {
    override suspend fun getPodcasts(term: String): PodcastResponse<Podcast> {
        return apiService.getPodcasts(term)
    }

    override suspend fun getPodcastById(trackId: Int): PodcastResponse<Episode> {
        return apiService.getPodcastById(trackId)
    }
}