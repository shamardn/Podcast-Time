package com.shamardn.podcasttime.ui.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.data.datasource.local.database.entity.RecentEntity
import com.shamardn.podcasttime.domain.usecase.DeleteRecentListUseCase
import com.shamardn.podcasttime.domain.usecase.DeletePodcastFromRecentUseCase
import com.shamardn.podcasttime.domain.usecase.GetRecentListUseCase
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import com.shamardn.podcasttime.ui.common.uistate.UiState
import com.shamardn.podcasttime.ui.common.ui_state_mapper.HistoryEntityUiStateMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getRecentListUseCase: GetRecentListUseCase,
    private val deleteRecentListUseCase: DeleteRecentListUseCase,
    private val deletePodcastFromRecentUseCase: DeletePodcastFromRecentUseCase,
    private val historyEntityUiStateMapper: HistoryEntityUiStateMapper,
) : ViewModel() {

    private val _podcasts = MutableStateFlow<List<RecentEntity>?>(null)
    val podcasts: StateFlow<List<RecentEntity>?> = _podcasts

    private val _historyUiState = MutableStateFlow(UiState(UiState().isEmpty))
    val historyUiState: StateFlow<UiState> = _historyUiState

    fun getHistoryPodcasts() = viewModelScope.launch(IO) {
        try {
            val response = getRecentListUseCase()
            _historyUiState.update {
                it.copy(
                    isLoading = false,
                    isSuccess = true,
                    podcastUiState = response.map { podcast ->
                        historyEntityUiStateMapper.reverseMap(podcast)
                    }
                )
            }
        } catch (e: Exception) {
            Log.e("HistoryViewModel", e.message.toString())
            onError(e.message.toString())
        }
    }

    private fun onError(message: String) {
        val errors = _historyUiState.value.error.toMutableList()
        errors.add(message)
        _historyUiState.update {
            it.copy(
                error = errors,
                isLoading = false,
                isFailed = true,
            )
        }
    }

    fun deleteHistoryList() = viewModelScope.launch {
        try {
            withContext(viewModelScope.coroutineContext) {
                _podcasts.value = null
                deleteRecentListUseCase()
            }

        } catch (e: Exception) {
            Log.e("HistoryViewModel", e.message.toString())
        }
    }

    fun deletePodcastFromHistory(podcast: PodcastUiState) = viewModelScope.launch {
        try {
            deletePodcastFromRecentUseCase(historyEntityUiStateMapper.map(podcast))

        } catch (e: Exception) {
            Log.e("HistoryViewModel", e.message.toString())
        }
    }
}