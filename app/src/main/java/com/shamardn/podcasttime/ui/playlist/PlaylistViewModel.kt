package com.shamardn.podcasttime.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.data.datasource.local.database.entity.PlaylistEntity
import com.shamardn.podcasttime.data.model.Resource
import com.shamardn.podcasttime.domain.repo.common.PodcastRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val podcastRepo: PodcastRepo,
) : ViewModel() {

    private var _playlists: MutableStateFlow<Resource<List<PlaylistEntity>>> = MutableStateFlow(Resource.Loading)
    val playlists: StateFlow<Resource<List<PlaylistEntity>>> = _playlists.asStateFlow()

    fun fetchAllPlaylists() = viewModelScope.launch(IO) {
       try {
           val playlistResponse = podcastRepo.getPlaylists()
           _playlists.emit(Resource.Success(playlistResponse))
       }catch (e: Exception){
           _playlists.emit(Resource.Error(e))
       }
    }
}