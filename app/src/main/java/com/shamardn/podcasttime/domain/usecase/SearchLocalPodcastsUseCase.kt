package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.data.datasource.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.domain.repo.common.PodcastRepo

class SearchLocalPodcastsUseCase (
    private val repo: PodcastRepo,
) {
    suspend operator fun invoke(text: String) : List<PodcastEntity> {
        return repo.searchLocalPodcastsByName(text)
    }
}