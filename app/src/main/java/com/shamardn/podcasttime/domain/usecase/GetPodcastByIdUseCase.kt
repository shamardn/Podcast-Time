package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.PodcastRepo
import javax.inject.Inject

class GetPodcastByIdUseCase @Inject constructor (private val repo: PodcastRepo) {
    suspend operator fun invoke(trackId: Int)  = repo.getPodcastById(trackId)
}