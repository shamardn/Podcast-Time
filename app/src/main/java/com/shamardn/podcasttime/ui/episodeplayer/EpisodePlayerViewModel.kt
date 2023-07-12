package com.shamardn.podcasttime.ui.episodeplayer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.data.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.domain.usecase.GetEpisodeByGuidUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodePlayerViewModel @Inject constructor(
    private val getEpisodeByGuidUseCase: GetEpisodeByGuidUseCase,
): ViewModel() {
    private val _episode = MutableStateFlow<EpisodeEntity?>(null)
    val episode: StateFlow<EpisodeEntity?> = _episode

    fun getEpisodeByGuid(guid: String){
        try {
            viewModelScope.launch {
                _episode.value = getEpisodeByGuidUseCase(guid)
            }
        }catch (e: Exception){
            Log.e("EpisodePlayerViewModel", e.message.toString())
        }
    }
}