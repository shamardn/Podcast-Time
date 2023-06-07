package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.entity.EpisodeDTO
import com.shamardn.podcasttime.domain.mapper.EpisodeMapper
import com.shamardn.podcasttime.domain.repo.PodcastRepo
import javax.inject.Inject

class SaveEpisodeToDownloadUseCase @Inject constructor(
    private val podcastRepo: PodcastRepo,
    private val episodeMapper: EpisodeMapper,
) {
    suspend operator fun invoke(episodeDTO: EpisodeDTO) {
        podcastRepo.insertEpisode(episodeMapper.map(episodeDTO))
    }
}