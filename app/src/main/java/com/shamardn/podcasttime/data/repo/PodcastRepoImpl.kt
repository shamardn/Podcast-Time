package com.shamardn.podcasttime.data.repo

import com.shamardn.podcasttime.data.local.database.dao.EpisodeDao
import com.shamardn.podcasttime.data.local.database.dao.PodcastDao
import com.shamardn.podcasttime.data.local.database.dao.SubscriptionsDao
import com.shamardn.podcasttime.data.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.data.local.database.entity.EpisodeAudio
import com.shamardn.podcasttime.data.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.data.remote.ApiService
import com.shamardn.podcasttime.domain.entity.EpisodeDTO
import com.shamardn.podcasttime.domain.entity.PodcastDTO
import com.shamardn.podcasttime.domain.entity.PodcastResponse
import com.shamardn.podcasttime.domain.repo.PodcastRepo
import javax.inject.Inject

class PodcastRepoImpl @Inject constructor(
    private val apiService: ApiService,
    private val episodeDao: EpisodeDao,
    private val podcastDao: PodcastDao,
    private val subscriptionsDao: SubscriptionsDao,
    ): PodcastRepo {
    override suspend fun getPodcasts(term: String): PodcastResponse<PodcastDTO> {
        return apiService.getPodcasts(term)
    }

    override suspend fun getPodcastById(trackId: Long): PodcastResponse<EpisodeDTO> {
        return apiService.getPodcastById(trackId)
    }

    override suspend fun insertEpisode(episodeEntity: EpisodeEntity) {
        episodeDao.insertEpisode(episodeEntity)
    }

    override suspend fun getDownloadedEpisodes(): List<EpisodeEntity> {
        return episodeDao.getEpisodes()
    }

    override suspend fun getMediaPodcasts(): List<EpisodeAudio> {
        return podcastDao.getMediaPodcasts()
    }

    override suspend fun saveAllPodcasts(episodes: List<EpisodeAudio>) {
        return podcastDao.insertAll(episodes)    }

    override suspend fun getSubscriptions(): List<PodcastEntity> {
        return subscriptionsDao.getSubscriptions()
    }

    override suspend fun subscribe(podcastEntity: PodcastEntity) {
        return subscriptionsDao.subscribe(podcastEntity)
    }

    override suspend fun deleteEpisode(episodeEntity: EpisodeEntity) {
        return episodeDao.deleteEpisode(episodeEntity)
    }
}