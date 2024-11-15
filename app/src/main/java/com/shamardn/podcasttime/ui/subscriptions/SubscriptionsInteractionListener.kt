package com.shamardn.podcasttime.ui.subscriptions

import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState

interface SubscriptionsInteractionListener {
    fun onClickPodcast(trackId: Long)
    fun onLongClickPodcast(podcast: PodcastUiState)
}