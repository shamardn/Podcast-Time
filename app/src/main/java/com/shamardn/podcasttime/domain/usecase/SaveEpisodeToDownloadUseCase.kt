package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.entity.EpisodeDTO
import com.shamardn.podcasttime.domain.mapper.EpisodeDTOMapper
import com.shamardn.podcasttime.domain.repo.PodcastRepo
import javax.inject.Inject

class SaveEpisodeToDownloadUseCase @Inject constructor(
    private val podcastRepo: PodcastRepo,
    private val episodeDTOMapper: EpisodeDTOMapper,
) {
    suspend operator fun invoke(episodeDTO: EpisodeDTO) {
        podcastRepo.insertEpisode(episodeDTOMapper.map(episodeDTO))
    }
}