package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.PodcastRepo

class GetMediaPodcastsUseCase (private val repo: PodcastRepo) {
    suspend operator fun invoke()  = repo.getMediaPodcasts()
}