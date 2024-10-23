package com.shamardn.podcasttime.ui.podcastdetails

import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState

interface PodcastDetailsInteractionListener {
    fun onClickEpisode(episodeUiState: EpisodeUiState)
    fun onClickDownload(episodeUiState: EpisodeUiState)
}