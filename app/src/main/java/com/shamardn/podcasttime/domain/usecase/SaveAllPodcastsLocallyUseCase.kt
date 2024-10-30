package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import com.shamardn.podcasttime.ui.common.mapper.PodcastUiStateMapper.Companion.toEntity
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import javax.inject.Inject

class SaveAllPodcastsLocallyUseCase @Inject constructor (
    private val repo: PodcastRepo
    ) {
    suspend operator fun invoke(podcasts: List<PodcastUiState>)  = repo.insertAllPodcasts(podcasts.map{it.toEntity()})
}