package com.shamardn.podcasttime.domain.repo

import com.shamardn.podcasttime.data.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.data.local.database.entity.EpisodeAudio
import com.shamardn.podcasttime.data.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.domain.entity.EpisodeDTO
import com.shamardn.podcasttime.domain.entity.PodcastDTO
import com.shamardn.podcasttime.domain.entity.PodcastResponse

interface PodcastRepo {
    suspend fun getPodcasts(term: String): PodcastResponse<PodcastDTO>
    suspend fun getPodcastById(trackId: Long): PodcastResponse<EpisodeDTO>
    suspend fun insertEpisode(episodeEntity: EpisodeEntity)
    suspend fun getDownloadedEpisodes(): List<EpisodeEntity>
    suspend fun getMediaPodcasts(): List<EpisodeAudio>
    suspend fun saveAllPodcasts(episodes: List<EpisodeAudio>)
    suspend fun getSubscriptions(): List<PodcastEntity>
    suspend fun subscribe(podcastEntity: PodcastEntity)
}