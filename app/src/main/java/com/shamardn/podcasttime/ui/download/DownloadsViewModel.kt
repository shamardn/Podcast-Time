package com.shamardn.podcasttime.ui.download

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.data.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.domain.usecase.DeleteDownloadedEpisodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(
    private val deleteDownloadedEpisodeUseCase: DeleteDownloadedEpisodeUseCase,
    ): ViewModel() {

    fun deleteEpisode(episode: EpisodeEntity) {
        try {
            viewModelScope.launch {
                deleteDownloadedEpisodeUseCase(episode)
            }
        } catch (e: Exception) {
            Log.e("DownloadsViewModel", e.message.toString())
        }
    }
}