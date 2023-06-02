package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.PodcastRepo
import javax.inject.Inject

class GetPodcastsUseCase @Inject constructor (private val repo: PodcastRepo) {
    suspend operator fun invoke(term: String)  = repo.getPodcasts(term)
}