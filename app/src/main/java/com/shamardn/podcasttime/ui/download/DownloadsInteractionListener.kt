package com.shamardn.podcasttime.ui.download

import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState

interface DownloadsInteractionListener {
    fun onEpisodeClick(currentEpisode: EpisodeUiState, position: Int)
    fun onDeleteEpisodeClick(currentEpisode: EpisodeUiState)
}