package com.shamardn.podcasttime.ui.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.data.model.Resource
import com.shamardn.podcasttime.domain.usecase.DeleteAllDownloadedEpisodesUseCase
import com.shamardn.podcasttime.domain.usecase.DeleteDownloadedEpisodeUseCase
import com.shamardn.podcasttime.domain.usecase.GetDownloadedEpisodesUseCase
import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(
    private val deleteDownloadedEpisodeUseCase: DeleteDownloadedEpisodeUseCase,
    private val deleteAllDownloadedEpisodesUseCase: DeleteAllDownloadedEpisodesUseCase,
    private val getDownloadedEpisodesUseCase: GetDownloadedEpisodesUseCase,
) : ViewModel() {

    private val _downloadedEpisodesUiState =
        MutableStateFlow<Resource<List<EpisodeUiState>>>(Resource.Loading)
    val downloadedEpisodesUiState: StateFlow<Resource<List<EpisodeUiState>>> =
        _downloadedEpisodesUiState.asStateFlow()

    fun deleteEpisode(episode: EpisodeUiState) = viewModelScope.launch {
        try {
            deleteDownloadedEpisodeUseCase(episode)
        } catch (e: Exception) {
            _downloadedEpisodesUiState.emit(Resource.Error(e))
        }
    }

    fun getDownloadedEpisodes() = viewModelScope.launch(IO) {
        try {
            val downloadedList = getDownloadedEpisodesUseCase()
            _downloadedEpisodesUiState.emit(Resource.Success(downloadedList))
        } catch (e: Exception) {
            _downloadedEpisodesUiState.emit(Resource.Error(e))
        }
    }

    fun deleteDownloadedEpisodesList() = viewModelScope.launch {
        try {
            deleteAllDownloadedEpisodesUseCase()
        } catch (e: Exception) {
            _downloadedEpisodesUiState.emit(Resource.Error(e))
        }
    }
}