package com.shamardn.podcasttime.ui.subscriptions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.data.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.domain.usecase.GetSubscriptionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SubscriptionsViewModel @Inject constructor(
    private val getSubscriptionsUseCase: GetSubscriptionsUseCase,
    ): ViewModel() {

    private val _podcasts = MutableStateFlow<List<PodcastEntity>?>(null)
    val podcasts: StateFlow<List<PodcastEntity>?> = _podcasts

    fun getSubscribedPodcasts() {
        try {
            viewModelScope.launch {
                withContext(viewModelScope.coroutineContext) {
                    _podcasts.value = getSubscriptionsUseCase()
                }
            }
        } catch (e: Exception) {
            Log.e("SubscriptionsViewModel", e.message.toString())
        }
    }
}