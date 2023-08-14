package com.shamardn.podcasttime.ui.subscriptions

import com.shamardn.podcasttime.data.local.database.entity.PodcastEntity

interface SubscriptionsInteractionListener {
    fun onClickPodcast(trackId: Long)
    fun onLongClickPodcast(podcast: PodcastEntity)
}