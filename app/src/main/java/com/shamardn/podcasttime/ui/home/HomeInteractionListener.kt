package com.shamardn.podcasttime.ui.home

import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState

interface HomeInteractionListener {
    fun onClickPodcast(podcast: PodcastUiState)
}