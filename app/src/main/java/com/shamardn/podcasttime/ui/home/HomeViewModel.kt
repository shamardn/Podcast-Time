package com.shamardn.podcasttime.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.PodcastTimeApplication
import com.shamardn.podcasttime.domain.usecase.GetPodcastsUseCase
import com.shamardn.podcasttime.ui.home.mapper.PodcastUiStateMapper
import com.shamardn.podcasttime.ui.home.uistate.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPodcastsUseCase: GetPodcastsUseCase,
    private val homeUiStateMapper: PodcastUiStateMapper,
): ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState

    private val _isOnline = MutableLiveData<Boolean>()
    val isOnline = _isOnline as LiveData<Boolean>
    fun getPodcasts(term: String){
        try {
            viewModelScope.launch {
                if (PodcastTimeApplication.isConnected) {
                    _isOnline.postValue(true)
                    val response = getPodcastsUseCase(term)
                    _homeUiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            podcastUiState = response.results.map { podcast ->
                                homeUiStateMapper.map(podcast)
                            }
                        )
                    }
                } else {
                    _isOnline.postValue(false)
                    _homeUiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = false,
                            isFailed = true,
                        )
                    }
                }
            }
        }catch (e: Exception){
            Log.e("HomeViewModel", e.message.toString())
            onError(e.message.toString())
        }
    }

    private fun onError(message: String) {
        val errors = _homeUiState.value.error.toMutableList()
        errors.add(message)
        _homeUiState.update {
            it.copy(
                error = errors,
                isLoading = false,
                isFailed = true,
            )
        }
    }
}