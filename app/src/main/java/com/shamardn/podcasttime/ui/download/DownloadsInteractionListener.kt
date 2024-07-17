package com.shamardn.podcasttime.ui.download

import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeEntity

interface DownloadsInteractionListener {
    fun onEpisodeClick(currentEpisode: EpisodeEntity)
    fun onDeleteEpisodeClick(currentEpisode: EpisodeEntity)
}