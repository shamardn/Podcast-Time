package com.shamardn.podcasttime.ui.podcastdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.domain.entity.EpisodeDTO
import com.shamardn.podcasttime.domain.entity.PodcastResponse
import com.shamardn.podcasttime.domain.usecase.GetPodcastByIdUseCase
import com.shamardn.podcasttime.domain.usecase.SaveEpisodeToDownloadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodcastDetailsViewModel @Inject constructor(
    private val getPodcastByIdUseCase: GetPodcastByIdUseCase,
    private val saveEpisodeToDownloadUseCase: SaveEpisodeToDownloadUseCase,
): ViewModel() {
    private val _episodes = MutableStateFlow<PodcastResponse<EpisodeDTO>?>(null)
    val episodes: StateFlow<PodcastResponse<EpisodeDTO>?> = _episodes

    fun getPodcastById(trackId: Int){
        try {
            viewModelScope.launch {
                _episodes.value = getPodcastByIdUseCase(trackId)
            }
        }catch (e: Exception){
            Log.e("PodcastDetailsViewModel", e.message.toString())
        }
    }

    suspend fun saveEpisodeToDownload(episodeDTO: EpisodeDTO) {
       try {
           viewModelScope.launch {
               saveEpisodeToDownloadUseCase(episodeDTO)
           }
       }catch (e: Exception){
           Log.e("PodcastDetailsViewModel", e.message.toString())
       }
    }

}