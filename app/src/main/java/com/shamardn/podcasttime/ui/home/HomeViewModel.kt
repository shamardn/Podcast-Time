package com.shamardn.podcasttime.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.PodcastTimeApplication
import com.shamardn.podcasttime.domain.usecase.GetLocalPodcastsUseCase
import com.shamardn.podcasttime.domain.usecase.GetPodcastsUseCase
import com.shamardn.podcasttime.domain.usecase.SavePodcastUseCase
import com.shamardn.podcasttime.ui.common.mapper.PodcastUiStateMapper
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import com.shamardn.podcasttime.ui.common.uistate.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPodcastsUseCase: GetPodcastsUseCase,
    private val getLocalPodcastsUseCase: GetLocalPodcastsUseCase,
    private val homeUiStateMapper: PodcastUiStateMapper,
    private val savePodcastUseCase: SavePodcastUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _isOnline = MutableLiveData<Boolean>()
    val isOnline = _isOnline as LiveData<Boolean>
    fun getLocalPodcasts() = viewModelScope.launch {
        _uiState.emit(UiState(isLoading = true))
        try {
            val response = getLocalPodcastsUseCase()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isSuccess = true,
                    podcastUiState = response.map { podcast ->
                        homeUiStateMapper.map(podcast)
                    }
                )
            }

        }catch (e: Exception){
            onError(e.message.toString())
        }
    }

    fun getRemotePodcasts() = viewModelScope.launch(IO) {
        _uiState.emit(UiState(isLoading = true))
        try {
            if (PodcastTimeApplication.isConnected) {
                _isOnline.postValue(true)
                val response = getPodcastsUseCase()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        podcastUiState = response.map { podcast ->
                            homeUiStateMapper.map(podcast)
                        }
                    )
                }
            } else {
                _isOnline.postValue(false)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        isFailed = true,
                    )
                }
            }

        } catch (e: Exception) {
            Log.e("HomeViewModel", e.message.toString())
            onError(e.message.toString())
        }
    }

    private fun onError(message: String) {
        val errors = _uiState.value.error.toMutableList()
        errors.add(message)
        _uiState.update {
            it.copy(
                error = errors,
                isLoading = false,
                isFailed = true,
            )
        }
    }

    fun savePodcast(podcast: PodcastUiState) = viewModelScope.launch(IO) {
        savePodcastUseCase(podcast)
    }
}