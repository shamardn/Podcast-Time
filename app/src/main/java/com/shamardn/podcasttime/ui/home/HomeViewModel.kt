package com.shamardn.podcasttime.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.domain.entity.Podcast
import com.shamardn.podcasttime.domain.entity.PodcastResponse
import com.shamardn.podcasttime.domain.usecase.GetPodcastsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPodcastsUseCase: GetPodcastsUseCase,
): ViewModel() {
    private val _podcasts = MutableStateFlow<PodcastResponse<Podcast>?>(null)
    val podcasts: StateFlow<PodcastResponse<Podcast>?> = _podcasts

    fun getPodcasts(term: String){
        try {
            viewModelScope.launch {
                _podcasts.value = getPodcastsUseCase(term)
            }
        }catch (e: Exception){
            Log.e("HomeViewModel", e.message.toString())
        }
    }

}