package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.PodcastRepo

class GetPodcastByIdUseCase(private val repo: PodcastRepo) {
    suspend operator fun invoke(trackId: Int)  = repo.getPodcastById(trackId)
}