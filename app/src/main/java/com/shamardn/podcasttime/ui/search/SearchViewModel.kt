package com.shamardn.podcasttime.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.domain.usecase.GetPodcastsUseCase
import com.shamardn.podcasttime.ui.search.mapper.SearchUiStateMapper
import com.shamardn.podcasttime.ui.search.uistate.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getPodcastsUseCase: GetPodcastsUseCase,
    private val searchUiStateMapper: SearchUiStateMapper,
    ): ViewModel() {
    private val _searchUiState = MutableStateFlow(SearchUiState(SearchUiState().isEmpty))
    val searchUiState: StateFlow<SearchUiState> = _searchUiState

    fun getPodcasts(term: String){
        try {
            viewModelScope.launch {
                val response = getPodcastsUseCase(term)
                _searchUiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        podcastUiState = response.results.map { podcast ->
                            searchUiStateMapper.map(podcast)

                        }
                    )
                }
            }
        }catch (e: Exception){
            Log.e("SearchViewModel", e.message.toString())
            onError(e.message.toString())
        }
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
    }

}