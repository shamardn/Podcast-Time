package com.shamardn.podcasttime.ui.podcastdetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.data.datasource.local.database.entity.HistoryEntity
import com.shamardn.podcasttime.data.model.Resource
import com.shamardn.podcasttime.domain.usecase.GetEpisodesByIdUseCase
import com.shamardn.podcasttime.domain.usecase.GetHistoryListUseCase
import com.shamardn.podcasttime.domain.usecase.GetPodcastByIdUseCase
import com.shamardn.podcasttime.domain.usecase.GetSubscriptionsUseCase
import com.shamardn.podcasttime.domain.usecase.SaveEpisodeToDownloadUseCase
import com.shamardn.podcasttime.domain.usecase.SaveToHistoryUseCase
import com.shamardn.podcasttime.domain.usecase.SubscribeUseCase
import com.shamardn.podcasttime.domain.usecase.UnsubscribeUseCase
import com.shamardn.podcasttime.ui.common.mapper.EpisodeUiStateMapper
import com.shamardn.podcasttime.ui.common.mapper.PodcastUiStateMapper
import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodcastDetailsViewModel @Inject constructor(
    private val subscribeUseCase: SubscribeUseCase,
    private val getSubscriptionsUseCase: GetSubscriptionsUseCase,
    private val unsubscribeUseCase: UnsubscribeUseCase,
    private val getHistoryListUseCase: GetHistoryListUseCase,
    private val saveToHistoryUseCase: SaveToHistoryUseCase,
    private val getPodcastByIdUseCase: GetPodcastByIdUseCase,
    private val getEpisodesByIdUseCase: GetEpisodesByIdUseCase,
    private val saveEpisodeToDownloadUseCase: SaveEpisodeToDownloadUseCase,
    private val uiStateMapper: PodcastUiStateMapper,
    private val episodeUiStateMapper: EpisodeUiStateMapper,

    ) : ViewModel() {
    private val _podcastUiState = MutableStateFlow<Resource<PodcastUiState>>(Resource.Loading)
    val podcastUiState: StateFlow<Resource<PodcastUiState>> = _podcastUiState.asStateFlow()

    private val _episodesUiState =
        MutableStateFlow<Resource<List<EpisodeUiState>>>(Resource.Loading)
    val episodesUiState: StateFlow<Resource<List<EpisodeUiState>>> = _episodesUiState.asStateFlow()

    private val _desc = MutableLiveData("")
    val desc: LiveData<String> = _desc

    fun getPodcastById(trackId: Long) = viewModelScope.launch(IO) {
        try {
            val podcastResponse = getPodcastByIdUseCase(trackId)
            _podcastUiState.emit(Resource.Success(uiStateMapper.map(podcastResponse)))
        } catch (e: Exception) {
            Log.e(TAG, "error in getPodcastById = ${e.message}")
            _podcastUiState.emit(Resource.Error(e))
        }
    }

    fun getEpisodeSById(trackId: Long) = viewModelScope.launch(IO) {
        try {
            val episodesResponse = getEpisodesByIdUseCase(trackId)
            _episodesUiState.emit(Resource.Success(episodeUiStateMapper.mapList(episodesResponse)))
            _desc.postValue(episodesResponse[1].description)
        } catch (e: Exception) {
            Log.e(TAG, "Exception in getEpisodeSById = ${e.message}")
            _episodesUiState.emit(Resource.Error(e))
        }
    }

    suspend fun onSubscribe(podcastUiState: PodcastUiState) {
        try {
            viewModelScope.launch {
                subscribeUseCase(podcastUiState)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }

    suspend fun saveToHistory(historyEntity: HistoryEntity) {
        try {
            viewModelScope.launch {
                saveToHistoryUseCase(historyEntity)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }

    suspend fun saveEpisodeToDownload(episodeUiState: EpisodeUiState) = viewModelScope.launch {
        try {
            saveEpisodeToDownloadUseCase(episodeUiState)
        } catch (e: Exception) {
            Log.e("MediaViewModel", e.message.toString())
        }
    }

    companion object {
        private const val TAG = "PodcastDetailsViewModel"
    }
}