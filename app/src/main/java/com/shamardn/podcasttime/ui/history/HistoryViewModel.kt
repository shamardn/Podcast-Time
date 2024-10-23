package com.shamardn.podcasttime.ui.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.data.datasource.local.database.entity.HistoryEntity
import com.shamardn.podcasttime.domain.usecase.DeleteHistoryListUseCase
import com.shamardn.podcasttime.domain.usecase.DeletePodcastFromHistoryUseCase
import com.shamardn.podcasttime.domain.usecase.GetHistoryListUseCase
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import com.shamardn.podcasttime.ui.common.uistate.UiState
import com.shamardn.podcasttime.ui.history.mapper.HistoryEntityMapper
import com.shamardn.podcasttime.ui.history.mapper.HistoryUiStateMapper
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
    private val getHistoryListUseCase: GetHistoryListUseCase,
    private val deleteHistoryListUseCase: DeleteHistoryListUseCase,
    private val deletePodcastFromHistoryUseCase: DeletePodcastFromHistoryUseCase,
    private val historyUiStateMapper: HistoryUiStateMapper,
    private val historyEntityMapper: HistoryEntityMapper,
) : ViewModel() {

    private val _podcasts = MutableStateFlow<List<HistoryEntity>?>(null)
    val podcasts: StateFlow<List<HistoryEntity>?> = _podcasts

    private val _historyUiState = MutableStateFlow(UiState(UiState().isEmpty))
    val historyUiState: StateFlow<UiState> = _historyUiState

    fun getHistoryPodcasts() = viewModelScope.launch(IO) {
        try {
            val response = getHistoryListUseCase()
            _historyUiState.update {
                it.copy(
                    isLoading = false,
                    isSuccess = true,
                    podcastUiState = response.map { podcast ->
                        historyUiStateMapper.map(podcast)
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
                deleteHistoryListUseCase()
            }

        } catch (e: Exception) {
            Log.e("HistoryViewModel", e.message.toString())
        }
    }

    fun deletePodcastFromHistory(podcast: PodcastUiState) = viewModelScope.launch {
        try {
            deletePodcastFromHistoryUseCase(historyEntityMapper.map(podcast))

        } catch (e: Exception) {
            Log.e("HistoryViewModel", e.message.toString())
        }
    }
}