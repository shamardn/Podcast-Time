package com.shamardn.podcasttime.ui.subscriptions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.data.model.Resource
import com.shamardn.podcasttime.domain.usecase.DeleteSubscriptionListUseCase
import com.shamardn.podcasttime.domain.usecase.GetSubscriptionsUseCase
import com.shamardn.podcasttime.domain.usecase.UnsubscribeUseCase
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionsViewModel @Inject constructor(
    private val getSubscriptionsUseCase: GetSubscriptionsUseCase,
    private val deleteSubscriptionListUseCase: DeleteSubscriptionListUseCase,
    private val unsubscribeUseCase: UnsubscribeUseCase,
) : ViewModel() {

    private val _subscriptionsUiState =
        MutableStateFlow<Resource<List<PodcastUiState>>>(Resource.Loading)
    val subscriptionsUiState: StateFlow<Resource<List<PodcastUiState>>> =
        _subscriptionsUiState.asStateFlow()

    fun getSubscribedPodcasts() = viewModelScope.launch(IO) {
        try {
            val response = getSubscriptionsUseCase()
            _subscriptionsUiState.emit(Resource.Success(response))
        } catch (e: Exception) {
            Log.e("SubscriptionsViewModel", e.message.toString())
            _subscriptionsUiState.emit(Resource.Error(e))
        }
    }

    fun unsubscribe(podcast: PodcastUiState) = viewModelScope.launch {
        try {
            unsubscribeUseCase(podcast)
        } catch (e: Exception) {
            Log.e("SubscriptionsViewModel", e.message.toString())
        }
    }

    fun deleteSubscriptionList() = viewModelScope.launch(IO) {
        try {
            deleteSubscriptionListUseCase()
        } catch (e: Exception) {
            Log.e("SubscriptionsViewModel", e.message.toString())
        }
    }
}