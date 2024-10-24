package com.shamardn.podcasttime.ui.episodeplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.data.datasource.local.database.entity.PlaylistEntity
import com.shamardn.podcasttime.data.model.Resource
import com.shamardn.podcasttime.domain.usecase.GetFavouritePlaylistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailsBottomSheetViewModel @Inject constructor(
    private val getFavouritePlaylistUseCase: GetFavouritePlaylistUseCase,
) : ViewModel() {

    private val _favouritePlaylist = MutableStateFlow<Resource<PlaylistEntity>>(Resource.Loading)
    val favouritePlaylist: StateFlow<Resource<PlaylistEntity>> = _favouritePlaylist.asStateFlow()

    fun getFavouritePlaylist() = viewModelScope.launch(IO) {
        try {
            val favouritePlaylist = getFavouritePlaylistUseCase()
            _favouritePlaylist.emit(Resource.Success(favouritePlaylist))
        } catch (e: Exception) {
            _favouritePlaylist.emit(Resource.Error(e))
        }
    }
}