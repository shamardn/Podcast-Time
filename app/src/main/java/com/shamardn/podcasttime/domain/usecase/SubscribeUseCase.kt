package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.data.datasource.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.domain.repo.PodcastRepo
import javax.inject.Inject

class SubscribeUseCase @Inject constructor (
    private val repo: PodcastRepo
    ) {
    suspend operator fun invoke(podcastEntity: PodcastEntity)  = repo.subscribe(podcastEntity)
}