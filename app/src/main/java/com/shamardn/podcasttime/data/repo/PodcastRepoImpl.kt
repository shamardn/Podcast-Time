package com.shamardn.podcasttime.data.repo

import com.shamardn.podcasttime.data.local.database.dao.EpisodeDao
import com.shamardn.podcasttime.data.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.data.remote.ApiService
import com.shamardn.podcasttime.domain.entity.EpisodeDTO
import com.shamardn.podcasttime.domain.entity.PodcastDTO
import com.shamardn.podcasttime.domain.entity.PodcastResponse
import com.shamardn.podcasttime.domain.repo.PodcastRepo
import javax.inject.Inject

class PodcastRepoImpl @Inject constructor(
    private val apiService: ApiService,
    private val episodeDao: EpisodeDao,
    ): PodcastRepo {
    override suspend fun getPodcasts(term: String): PodcastResponse<PodcastDTO> {
        return apiService.getPodcasts(term)
    }

    override suspend fun getPodcastById(trackId: Int): PodcastResponse<EpisodeDTO> {
        return apiService.getPodcastById(trackId)
    }

    override suspend fun getEpisodeByGuid(guid: String): EpisodeEntity {
        return episodeDao.getEpisodeByGuid(guid)
    }

    override suspend fun insertEpisode(episodeEntity: EpisodeEntity) {
        episodeDao.insertEpisode(episodeEntity)
    }

    override suspend fun getDownloadedEpisodes(): List<EpisodeEntity> {
        return episodeDao.getEpisodes()
    }
}