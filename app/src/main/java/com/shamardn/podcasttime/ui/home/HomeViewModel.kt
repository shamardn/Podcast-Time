package com.shamardn.podcasttime.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.domain.entity.PodcastDTO
import com.shamardn.podcasttime.domain.entity.PodcastResponse
import com.shamardn.podcasttime.domain.usecase.GetPodcastsUseCase
import com.shamardn.podcasttime.util.ConnectionTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPodcastsUseCase: GetPodcastsUseCase,
    private val connectionTracker: ConnectionTracker,
): ViewModel() {
    private val _podcasts = MutableStateFlow<PodcastResponse<PodcastDTO>?>(null)
    val podcasts: StateFlow<PodcastResponse<PodcastDTO>?> = _podcasts

    private val _isOnline = MutableLiveData<Boolean>()
    val isOnline = _isOnline as LiveData<Boolean>
    fun getPodcasts(term: String){
        try {
            viewModelScope.launch {
                if (connectionTracker.isInternetConnectionAvailable()) {
                    _podcasts.value = getPodcastsUseCase(term)
                    _isOnline.postValue(true)
                } else {
                    //TODO handle error state
                    _isOnline.postValue(false)
                }
            }
        }catch (e: Exception){
            Log.e("HomeViewModel", e.message.toString())
        }
    }
}