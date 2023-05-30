package com.shamardn.podcasttime.ui.podcastdetails

interface PodcastDetailsInteractionListener {
    fun onClickEpisode(episodeUrl: String, artworkUrl: String, podcastTitle: String, episode: String)
}