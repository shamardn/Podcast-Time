package com.shamardn.podcasttime.ui.search

import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState

interface SearchInteractionListener {
    fun onClickPodcast(podcast: PodcastUiState)
}