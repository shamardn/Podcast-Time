package com.shamardn.podcasttime.domain.repo

import com.shamardn.podcasttime.domain.entity.Episode
import com.shamardn.podcasttime.domain.entity.Podcast
import com.shamardn.podcasttime.domain.entity.PodcastResponse

interface PodcastRepo {
    suspend fun getPodcasts(term: String): PodcastResponse<Podcast>
    suspend fun getPodcastById(trackId: Int): PodcastResponse<Episode>
}