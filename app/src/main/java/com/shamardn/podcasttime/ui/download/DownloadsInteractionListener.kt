package com.shamardn.podcasttime.ui.download

interface DownloadsInteractionListener {
    fun onEpisodeClick(episodeUrl: String,
                       artworkUrl: String,
                       podcastTitle: String,
                       episode: String)
    fun onDownloadedEpisodeIcon()
}