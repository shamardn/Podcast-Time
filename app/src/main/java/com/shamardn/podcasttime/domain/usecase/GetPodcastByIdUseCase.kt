package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.common.PodcastRepo

class GetPodcastByIdUseCase(private val repo: PodcastRepo) {
    suspend operator fun invoke(trackId: Long)  = repo.getPodcastById(trackId)
}