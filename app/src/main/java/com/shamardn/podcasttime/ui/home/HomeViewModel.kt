package com.shamardn.podcasttime.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.PodcastTimeApplication
import com.shamardn.podcasttime.data.model.Resource
import com.shamardn.podcasttime.domain.usecase.GetLocalPodcastsUseCase
import com.shamardn.podcasttime.domain.usecase.GetPodcastsUseCase
import com.shamardn.podcasttime.domain.usecase.SavePodcastUseCase
import com.shamardn.podcasttime.ui.common.mapper.PodcastUiStateMapper
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
    private val homeUiStateMapper: PodcastUiStateMapper,
    private val savePodcastUseCase: SavePodcastUseCase,
) : ViewModel() {
    private val _homeUiState = MutableStateFlow<Resource<List<PodcastUiState>>>(Resource.Loading)
    val homeUiState: StateFlow<Resource<List<PodcastUiState>>> = _homeUiState.asStateFlow()

    private val _isOnline = MutableLiveData<Boolean>()
    val isOnline = _isOnline as LiveData<Boolean>

    fun getLocalPodcasts() = viewModelScope.launch(IO) {
        try {
            val response = getLocalPodcastsUseCase()
            _homeUiState.emit(Resource.Success(homeUiStateMapper.mapList(response)))
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
                _homeUiState.emit(Resource.Success(homeUiStateMapper.mapList(response)))
            } else {
                _isOnline.postValue(false)
                _homeUiState.emit(Resource.Error(Exception("No Internet Connection")))
            }
        } catch (e: Exception) {
            val msg = e.message.toString()
            _homeUiState.emit(Resource.Error(Exception(msg)))
        }
    }
    fun savePodcast(podcast: PodcastUiState) = viewModelScope.launch(IO) {
        savePodcastUseCase(podcast)
    }
}