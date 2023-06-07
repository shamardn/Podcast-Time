package com.shamardn.podcasttime.ui.podcastdetails

import com.shamardn.podcasttime.domain.entity.EpisodeDTO

interface PodcastDetailsInteractionListener {
    fun onClickEpisode(episodeUrl: String, artworkUrl: String, podcastTitle: String, episode: String)

    fun onClickDownload(episodeDTO: EpisodeDTO)
}