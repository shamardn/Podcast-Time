package com.shamardn.podcasttime.ui.search.uistate

data class SearchUiState(
    val isLoading: Boolean = true,
    val error : List<String> = emptyList(),
    val isFailed : Boolean = false,
    val isSuccess : Boolean = false,
    val isEmpty: Boolean = true,
    val podcastUiState: List<PodcastUiState> = emptyList(),
)
