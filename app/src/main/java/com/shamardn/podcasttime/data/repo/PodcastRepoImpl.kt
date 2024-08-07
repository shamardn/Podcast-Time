package com.shamardn.podcasttime.data.repo

import com.shamardn.podcasttime.data.datasource.local.database.dao.EpisodeDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.HistoryDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.PodcastDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.SubscriptionsDao
import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeAudio
import com.shamardn.podcasttime.data.datasource.local.database.entity.HistoryEntity
import com.shamardn.podcasttime.data.datasource.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.data.datasource.remote.ApiService
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
    private val historyDao: HistoryDao,
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

    override suspend fun deleteEpisode(episodeEntity: EpisodeEntity) {
        return episodeDao.deleteEpisode(episodeEntity)
    }

    override suspend fun saveAllPodcasts(episodes: List<EpisodeAudio>) {
        return podcastDao.insertAll(episodes)    }

    override suspend fun getSubscriptions(): List<PodcastEntity> {
        return subscriptionsDao.getSubscriptions()
    }

    override suspend fun subscribe(podcastEntity: PodcastEntity) {
        return subscriptionsDao.subscribe(podcastEntity)
    }

    override suspend fun unsubscribe(podcast: PodcastEntity) {
        return subscriptionsDao.unsubscribe(podcast)
    }

    override suspend fun deleteSubscriptionList() {
        return subscriptionsDao.deleteSubscriptionList()
    }

    //region History
    override suspend fun saveToHistory(historyEntity: HistoryEntity) {
        return historyDao.saveToHistory(historyEntity)
    }

    override suspend fun getHistoryList(): List<HistoryEntity> {
        return historyDao.getHistoryList()
    }

    override suspend fun deletePodcastFromHistory(podcast: HistoryEntity) {
        return historyDao.deletePodcastFromHistory(podcast)
    }
    override suspend fun deleteHistoryList() {
        return historyDao.deleteHistoryList()
    }

    //endregion
}