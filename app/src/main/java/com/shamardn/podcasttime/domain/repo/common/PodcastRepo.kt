package com.shamardn.podcasttime.domain.repo.common

import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeDownloadEntity
import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.data.datasource.local.database.entity.RecentEntity
import com.shamardn.podcasttime.data.datasource.local.database.entity.PlaylistEntity
import com.shamardn.podcasttime.data.datasource.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.data.datasource.local.database.entity.SubscriptionsEntity
import com.shamardn.podcasttime.data.datasource.remote.dtos.EpisodeDTO
import com.shamardn.podcasttime.data.datasource.remote.dtos.PodcastDTO
import com.shamardn.podcasttime.data.datasource.remote.dtos.PodcastResponse

interface PodcastRepo {
    suspend fun insertAllPodcasts(podcasts: List<PodcastEntity>)
    suspend fun insertAllEpisodes(episodes: List<EpisodeEntity>)
    suspend fun insertPodcast(podcast: PodcastEntity)
    suspend fun refreshRemotePodcasts(): PodcastResponse<PodcastDTO>
    suspend fun searchLocalPodcastsByName(text: String): List<PodcastEntity>
    suspend fun refreshEpisodesById(trackId: Long): PodcastResponse<EpisodeDTO>
    suspend fun getLocalPodcasts(): List<PodcastEntity>
    suspend fun getPodcastById(podcastId: Long): PodcastEntity
    suspend fun deleteRecentList()
    suspend fun deletePodcastFromRecent(podcast: RecentEntity)

    suspend fun deleteSubscriptionList()
    suspend fun getRecentList(): List<RecentEntity>
    suspend fun getLocalEpisodes(): List<EpisodeEntity>
    suspend fun getAllDownloadedEpisodes(): List<EpisodeDownloadEntity>

    suspend fun unsubscribe(subscriptionsEntity: SubscriptionsEntity)
    suspend fun saveToRecent(recentEntity: RecentEntity)
    //    suspend fun getRemotePodcastById(trackId: Long): PodcastResponse<EpisodeDTO>
    suspend fun saveEpisodeToDownload(episode: EpisodeDownloadEntity)

    suspend fun getSubscriptions(): List<SubscriptionsEntity>
    suspend fun subscribe(subscriptionsEntity: SubscriptionsEntity)
    suspend fun deleteDownloadedEpisode(episode: EpisodeDownloadEntity)
    suspend fun deleteAllEpisodes()
    suspend fun deleteAllDownloadedEpisodes()

    suspend fun getPlaylists(): List<PlaylistEntity>
    suspend fun addNewPlaylist(playlistName: String)
    suspend fun addEpisodeToPlaylist(episode: EpisodeEntity,  playList: PlaylistEntity)
    suspend fun getFavouritePlaylist(): PlaylistEntity
}