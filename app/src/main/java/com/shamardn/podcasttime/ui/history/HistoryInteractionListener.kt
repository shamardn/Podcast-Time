package com.shamardn.podcasttime.ui.history

import com.shamardn.podcasttime.ui.history.uistate.PodcastUiState

interface HistoryInteractionListener {
    fun onClickPodcast(trackId: Long)
    fun onDeletePodcastFromHistoryClick(podcast: PodcastUiState)
}