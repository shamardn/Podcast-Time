package com.shamardn.podcasttime.ui.podcastdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.data.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.domain.entity.EpisodeDTO
import com.shamardn.podcasttime.domain.entity.PodcastResponse
import com.shamardn.podcasttime.domain.usecase.SubscribeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodcastDetailsViewModel @Inject constructor(
    private val subscribeUseCase: SubscribeUseCase,
    ): ViewModel() {

    private val _episodes = MutableStateFlow<PodcastResponse<EpisodeDTO>?>(null)
    val episodes: StateFlow<PodcastResponse<EpisodeDTO>?> = _episodes

    suspend fun onSubscribe(podcastEntity: PodcastEntity) {
        try {
            viewModelScope.launch {
                subscribeUseCase(podcastEntity)
            }
        } catch(e: Exception) {
            Log.e("PodcastDetailsViewModel", e.message.toString())
        }
    }
}