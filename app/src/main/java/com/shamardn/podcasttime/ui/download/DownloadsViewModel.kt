package com.shamardn.podcasttime.ui.download

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.data.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.domain.usecase.GetDownloadedEpisodesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(
    private val getDownloadedEpisodesUseCase: GetDownloadedEpisodesUseCase,
): ViewModel() {
    private val _episodes = MutableStateFlow<List<EpisodeEntity>?>(null)
    val episodes: StateFlow<List<EpisodeEntity>?> = _episodes

    fun getDownloadedEpisodes(){
        try {
            viewModelScope.launch { _episodes.value = getDownloadedEpisodesUseCase()
            }
        }catch (e: Exception){
            Log.e("DownloadsViewModel", e.message.toString())
        }
    }
}