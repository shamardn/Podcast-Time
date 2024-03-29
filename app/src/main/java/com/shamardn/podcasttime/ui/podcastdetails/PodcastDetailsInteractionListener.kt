package com.shamardn.podcasttime.ui.podcastdetails

import com.shamardn.podcasttime.domain.entity.EpisodeDTO

interface PodcastDetailsInteractionListener {
    fun onClickEpisode(currentEpisode: EpisodeDTO)

    fun onClickDownload(episodeDTO: EpisodeDTO)
}