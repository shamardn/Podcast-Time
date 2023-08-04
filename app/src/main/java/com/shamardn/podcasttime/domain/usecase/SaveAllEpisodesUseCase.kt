package com.shamardn.podcasttime.domain.usecase

import com.shamardn.podcasttime.domain.entity.EpisodeDTO
import com.shamardn.podcasttime.domain.mapper.EpisodeAudioMapper
import com.shamardn.podcasttime.domain.repo.PodcastRepo
import javax.inject.Inject

class SaveAllEpisodesUseCase @Inject constructor(
    private val podcastRepo: PodcastRepo,
    private val episodeAudioMapper: EpisodeAudioMapper,
) {
    suspend operator fun invoke(episodesDTO: List<EpisodeDTO>) {
        podcastRepo.saveAllPodcasts(episodeAudioMapper.mapList(episodesDTO))
    }
}