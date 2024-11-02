package com.shamardn.podcasttime.data.repo.common

import com.shamardn.podcasttime.data.datasource.local.database.dao.DownloadDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.EpisodeDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.PlaylistDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.PodcastDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.RecentDao
import com.shamardn.podcasttime.data.datasource.local.database.dao.SubscriptionsDao
import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeDownloadEntity
import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.data.datasource.local.database.entity.PlaylistEntity
import com.shamardn.podcasttime.data.datasource.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.data.datasource.local.database.entity.RecentEntity
import com.shamardn.podcasttime.data.datasource.local.database.entity.SubscriptionsEntity
import com.shamardn.podcasttime.data.datasource.remote.ApiService
import com.shamardn.podcasttime.data.datasource.remote.IpApiService
import com.shamardn.podcasttime.data.datasource.remote.dtos.EpisodeDTO
import com.shamardn.podcasttime.data.datasource.remote.dtos.PodcastDTO
import com.shamardn.podcasttime.data.datasource.remote.dtos.PodcastResponse
import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import com.shamardn.podcasttime.util.Constants.FAVOURITE_PLAYLIST
import com.shamardn.podcasttime.util.Constants.RECENTLY_PLAYED
import kotlinx.coroutines.delay
import javax.inject.Inject

class PodcastRepoImpl @Inject constructor(
    private val apiService: ApiService,
    private val ipApiService: IpApiService,
    private val episodeDao: EpisodeDao,
    private val podcastDao: PodcastDao,
    private val subscriptionsDao: SubscriptionsDao,
    private val recentDao: RecentDao,
    private val downloadDao: DownloadDao,
    private val playlistDao: PlaylistDao,
) : PodcastRepo {
    override suspend fun refreshRemotePodcasts(): PodcastResponse<PodcastDTO> {
        return try {
            val data = apiService.getPodcasts("podcast", getUserCountryCode()).results
            val podcastResponse = PodcastResponse(resultCount = data[0].trackCount, results = data)
            podcastResponse
        } catch (e: Exception) {
            delay(5000)
            throw e
        }
    }

    override suspend fun searchLocalPodcastsByName(text: String): List<PodcastEntity> {
        return podcastDao.searchLocalPodcastsByName(text)
    }

    override suspend fun getUserCountryCode() = ipApiService.getUserLocation().country_code

    override suspend fun insertAllPodcasts(podcasts: List<PodcastEntity>) {
        podcastDao.insertAllPodcasts(podcasts)
    }

    override suspend fun insertAllEpisodes(episodes: List<EpisodeEntity>) {
        episodeDao.insertAllEpisodes(episodes)
    }

    override suspend fun insertPodcast(podcast: PodcastEntity) {
        podcastDao.insertPodcast(podcast)
    }

    override suspend fun refreshEpisodesById(trackId: Long): PodcastResponse<EpisodeDTO> {
        val data = apiService.getEpisodesById(trackId).results
        val podcastResponse =
            PodcastResponse(resultCount = data[0].trackCount, results = data)
        return podcastResponse
    }

    override suspend fun getLocalPodcasts(): List<PodcastEntity> {
        return podcastDao.getLocalPodcasts()
    }

    override suspend fun getPodcastById(podcastId: Long): PodcastEntity {
        return podcastDao.getPodcastById(podcastId)
    }

    override suspend fun deleteDownloadedEpisode(episode: EpisodeDownloadEntity) {
        return downloadDao.deleteDownloadedEpisode(episode)
    }

    override suspend fun deleteAllEpisodes() {
        return episodeDao.deleteAllEpisodes()
    }

    override suspend fun deleteAllDownloadedEpisodes() {
        return downloadDao.deleteAllDownloadedEpisodes()
    }

    override suspend fun deleteRecentList() {
        return recentDao.deleteRecentList()
    }

    override suspend fun deletePodcastFromRecent(podcast: RecentEntity) {
        return recentDao.deletePodcastFromRecent(podcast)
    }

    override suspend fun deleteSubscriptionList() {
        return subscriptionsDao.deleteSubscriptionList()
    }

    override suspend fun getRecentList(): List<RecentEntity> {
        return recentDao.getRecentList()
    }

    override suspend fun saveEpisodeToDownload(episode: EpisodeDownloadEntity) {
        downloadDao.insertEpisode(episode)
    }

    override suspend fun getLocalEpisodes(): List<EpisodeEntity> {
        return episodeDao.getAllEpisodes()
    }

    override suspend fun getAllDownloadedEpisodes(): List<EpisodeDownloadEntity> {
        return downloadDao.getAllDownloadedEpisodes()
    }

    override suspend fun getSubscriptions(): List<SubscriptionsEntity> {
        return subscriptionsDao.getAllSubscriptionsOrderedByName()
    }

    override suspend fun subscribe(subscriptionsEntity: SubscriptionsEntity) {
        return subscriptionsDao.subscribe(subscriptionsEntity)
    }

    override suspend fun unsubscribe(subscriptionsEntity: SubscriptionsEntity) {
        return subscriptionsDao.unsubscribe(subscriptionsEntity)
    }


    override suspend fun saveToRecent(recentEntity: RecentEntity) {
        return recentDao.saveToRecent(recentEntity)
    }

    override suspend fun getPlaylists(): List<PlaylistEntity> {
        val savedPlaylists = playlistDao.getAllPlaylists()
        if (savedPlaylists.isNotEmpty()) {
            return savedPlaylists
        } else {
            playlistDao.insertPlaylist(PlaylistEntity(FAVOURITE_PLAYLIST, mutableListOf()))
            playlistDao.insertPlaylist(PlaylistEntity(RECENTLY_PLAYED, mutableListOf()))
            return listOf(
                PlaylistEntity(FAVOURITE_PLAYLIST, mutableListOf()),
                PlaylistEntity(RECENTLY_PLAYED, mutableListOf())
            )
        }
    }

    override suspend fun addNewPlaylist(playlistName: String) {
        playlistDao.insertPlaylist(PlaylistEntity(playlistName, mutableListOf()))
    }

    override suspend fun addEpisodeToPlaylist(episode: EpisodeEntity, playList: PlaylistEntity) {
        playList.playlistEpisodes.add(episode)
        playlistDao.insertPlaylist(playList)
    }

    override suspend fun getFavouritePlaylist(): PlaylistEntity {
        val favouritePlaylist = playlistDao.getPlaylistByName(FAVOURITE_PLAYLIST)
        return favouritePlaylist

    }

}