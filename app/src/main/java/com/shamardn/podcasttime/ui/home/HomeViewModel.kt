package com.shamardn.podcasttime.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.PodcastTimeApplication
import com.shamardn.podcasttime.data.model.Resource
import com.shamardn.podcasttime.domain.usecase.GetLocalPodcastsUseCase
import com.shamardn.podcasttime.domain.usecase.GetPodcastsUseCase
import com.shamardn.podcasttime.domain.usecase.GetRecentPodcastsUseCase
import com.shamardn.podcasttime.domain.usecase.SaveRecentPodcastUseCase
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPodcastsUseCase: GetPodcastsUseCase,
    private val getLocalPodcastsUseCase: GetLocalPodcastsUseCase,
    private val saveRecentPodcastUseCase: SaveRecentPodcastUseCase,
    private val getRecentPodcastsUseCase: GetRecentPodcastsUseCase,
) : ViewModel() {
    private val _homeUiState = MutableStateFlow<Resource<List<PodcastUiState>>>(Resource.Loading)
    val homeUiState: StateFlow<Resource<List<PodcastUiState>>> = _homeUiState.asStateFlow()

    private val _recentUiState = MutableStateFlow<Resource<List<PodcastUiState>>>(Resource.Loading)
    val recentUiState: StateFlow<Resource<List<PodcastUiState>>> = _recentUiState.asStateFlow()

    private val _isOnline = MutableLiveData<Boolean>()
    val isOnline = _isOnline as LiveData<Boolean>

    fun getLocalPodcasts() = viewModelScope.launch(IO) {
        try {
            val response = getLocalPodcastsUseCase()
            _homeUiState.emit(Resource.Success(response))
        }catch (e: Exception){
            val msg = e.message.toString()
            _homeUiState.emit(Resource.Error(Exception(msg)))
        }
    }

    fun getRemotePodcasts() = viewModelScope.launch(IO) {
        try {
            if (PodcastTimeApplication.isConnected) {
                _isOnline.postValue(true)
                val response = getPodcastsUseCase()
                _homeUiState.emit(Resource.Success(response))
            } else {
                _isOnline.postValue(false)
                _homeUiState.emit(Resource.Error(Exception("No Internet Connection")))
            }
        } catch (e: Exception) {
            val msg = e.message.toString()
            _homeUiState.emit(Resource.Error(Exception(msg)))
        }
    }

    fun getRecentPodcast() = viewModelScope.launch(IO) {
        try {
            val recentPodcasts = getRecentPodcastsUseCase()
            _recentUiState.emit(Resource.Success(recentPodcasts))
        }catch (e: Exception){
            val msg = e.message.toString()
            _recentUiState.emit(Resource.Error(Exception(msg)))
        }

    }

    fun saveRecentPodcast(podcast: PodcastUiState) = viewModelScope.launch(IO) {
        saveRecentPodcastUseCase(podcast)
    }
}