package com.shamardn.podcasttime.ui.history.uistate

data class HistoryUiState(
    val isLoading: Boolean = true,
    val error : List<String> = emptyList(),
    val isFailed : Boolean = false,
    val isSuccess : Boolean = false,
    val isEmpty: Boolean = true,
    val podcastUiState: List<PodcastUiState> = emptyList(),
)
