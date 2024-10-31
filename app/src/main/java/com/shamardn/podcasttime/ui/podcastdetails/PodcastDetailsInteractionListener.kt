package com.shamardn.podcasttime.ui.podcastdetails

import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState

interface PodcastDetailsInteractionListener {
    fun onClickEpisode(episode: EpisodeUiState)
    fun onClickDownload(episode: EpisodeUiState)
}