package com.shamardn.podcasttime.ui.subscriptions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.data.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.domain.usecase.DeleteSubscriptionListUseCase
import com.shamardn.podcasttime.domain.usecase.GetSubscriptionsUseCase
import com.shamardn.podcasttime.domain.usecase.UnsubscribeUseCase
import com.shamardn.podcasttime.ui.subscriptions.mapper.SubscriptionsUiStateMapper
import com.shamardn.podcasttime.ui.subscriptions.uistate.PodcastUiState
import com.shamardn.podcasttime.ui.subscriptions.uistate.SubscriptionsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SubscriptionsViewModel @Inject constructor(
    private val getSubscriptionsUseCase: GetSubscriptionsUseCase,
    private val deleteSubscriptionListUseCase: DeleteSubscriptionListUseCase,
    private val unsubscribeUseCase: UnsubscribeUseCase,
    private val subscriptionsUiStateMapper: SubscriptionsUiStateMapper,
) : ViewModel() {

    private val _subscriptionsUiState = MutableStateFlow(SubscriptionsUiState(SubscriptionsUiState().isEmpty))
    val subscriptionsUiState: StateFlow<SubscriptionsUiState> = _subscriptionsUiState


    fun getSubscribedPodcasts() {
        try {
            viewModelScope.launch {
                withContext(viewModelScope.coroutineContext) {
                    val response = getSubscriptionsUseCase()
                    _subscriptionsUiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            podcastUiState = response.map { podcast ->
                                subscriptionsUiStateMapper.map(podcast)
                            }
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("SubscriptionsViewModel", e.message.toString())
            onError(e.message.toString())
        }
    }

    private fun onError(message: String) {
        val errors = _subscriptionsUiState.value.error.toMutableList()
        errors.add(message)
        _subscriptionsUiState.update {
            it.copy(
                error = errors,
                isLoading = false,
                isFailed = true,
            )
        }
    }

    fun unsubscribe(podcast: PodcastUiState) {
        try {
            viewModelScope.launch {
                unsubscribeUseCase(
                    PodcastEntity(
                        trackId = podcast.trackId,
                        artistName = podcast.artistName,
                        collectionName = podcast.trackName,
                        artworkUrl100 = podcast.artworkUrl100,
                        primaryGenreName = podcast.primaryGenreName,
                        releaseDate = podcast.releaseDate,
                        trackCount = podcast.trackCount,
                        trackName = podcast.trackName,
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("SubscriptionsViewModel", e.message.toString())
        }
    }

    fun deleteSubscriptionList() {
        try {
            viewModelScope.launch {
                withContext(viewModelScope.coroutineContext) {
                    deleteSubscriptionListUseCase()
                }
            }
        } catch (e: Exception) {
            Log.e("SubscriptionsViewModel", e.message.toString())
        }
    }
}