package com.shamardn.podcasttime.domain.repo

import com.shamardn.podcasttime.data.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.domain.entity.EpisodeDTO
import com.shamardn.podcasttime.domain.entity.PodcastDTO
import com.shamardn.podcasttime.domain.entity.PodcastResponse

interface PodcastRepo {
    suspend fun getPodcasts(term: String): PodcastResponse<PodcastDTO>
    suspend fun getPodcastById(trackId: Int): PodcastResponse<EpisodeDTO>
    suspend fun insertEpisode(episodeEntity: EpisodeEntity)
}