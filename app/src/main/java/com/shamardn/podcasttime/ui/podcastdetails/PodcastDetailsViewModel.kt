package com.shamardn.podcasttime.ui.podcastdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.domain.entity.Episode
import com.shamardn.podcasttime.domain.entity.PodcastResponse
import com.shamardn.podcasttime.domain.usecase.GetPodcastByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodcastDetailsViewModel @Inject constructor(
    private val getPodcastByIdUseCase: GetPodcastByIdUseCase,
): ViewModel() {
    private val _episodes = MutableStateFlow<PodcastResponse<Episode>?>(null)
    val episodes: StateFlow<PodcastResponse<Episode>?> = _episodes

    fun getPodcastById(trackId: Int){
        try {
            viewModelScope.launch {
                _episodes.value = getPodcastByIdUseCase(trackId)
            }
        }catch (e: Exception){
            Log.e("PodcastDetailsViewModel", e.message.toString())
        }
    }

}