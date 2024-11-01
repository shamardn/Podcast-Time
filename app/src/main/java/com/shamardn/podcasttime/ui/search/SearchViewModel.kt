package com.shamardn.podcasttime.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.domain.usecase.GetLocalPodcastsUseCase
import com.shamardn.podcasttime.domain.usecase.SaveRecentPodcastUseCase
import com.shamardn.podcasttime.domain.usecase.SearchLocalPodcastsUseCase
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import com.shamardn.podcasttime.ui.common.uistate.UiState
import com.shamardn.podcasttime.util.CrashlyticsUtils
import com.shamardn.podcasttime.util.SearchException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchLocalPodcastsUseCase: SearchLocalPodcastsUseCase,
    private val getLocalPodcastsUseCase: GetLocalPodcastsUseCase,
    private val saveRecentPodcastUseCase: SaveRecentPodcastUseCase,
    ): ViewModel() {
    private val _searchUiState = MutableStateFlow(UiState(UiState().isEmpty))
    val searchUiState: StateFlow<UiState> = _searchUiState

    fun searchPodcastsLocally(term: String) = viewModelScope.launch {
        try {
                val response = searchLocalPodcastsUseCase(term)
                _searchUiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        podcastUiState = response
                    )
                }

        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
            onError(e.message.toString())
        }
    }

    fun fetchPodcastsLocally() = viewModelScope.launch {
        try {
                val response = getLocalPodcastsUseCase()
                _searchUiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        podcastUiState = response
                    )
                }

        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
            onError(e.message.toString())
        }
    }

    private fun logSearchIssueToCrashlytics(msg: String) {
        CrashlyticsUtils.sendCustomLogToCrashlytics<SearchException>(
            msg,
            CrashlyticsUtils.SEARCH_KEY to msg,
        )
    }
    private fun onError(message: String) {
        val errors = _searchUiState.value.error.toMutableList()
        errors.add(message)
        _searchUiState.update {
            it.copy(
                error = errors,
                isLoading = false,
                isFailed = true,
            )
        }
        logSearchIssueToCrashlytics(message)
    }

    fun saveRecentPodcast(podcast: PodcastUiState) = viewModelScope.launch(IO) {
        saveRecentPodcastUseCase(podcast)
    }

    companion object{
        private const val TAG = "SearchViewModel"
    }

}