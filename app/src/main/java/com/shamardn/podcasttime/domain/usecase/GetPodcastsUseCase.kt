package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.PodcastRepo

class GetPodcastsUseCase (private val repo: PodcastRepo) {
    suspend operator fun invoke(term: String)  = repo.getPodcasts(term)
}