package com.shamardn.podcasttime.ui.common.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import com.shamardn.podcasttime.data.model.Resource
import com.shamardn.podcasttime.player.PlayerController
import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState
import com.shamardn.podcasttime.util.PlayerEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    player: ExoPlayer,
    @ApplicationContext context: Context,
    private var sharedPreferences: SharedPreferences,
) : ViewModel() {

    private var _isBottomSheetOpened = MutableLiveData(false)
    val isBottomSheetOpened: LiveData<Boolean> = _isBottomSheetOpened


    private var _currentEpisode = MutableStateFlow<Resource<EpisodeUiState>>(Resource.Loading)
    val currentEpisode : StateFlow<Resource<EpisodeUiState>> = _currentEpisode.asStateFlow()

    private var _isViewModelStart = MutableStateFlow(false)
    val isViewModelStart = _currentEpisode.asStateFlow()

    private var _currentMediaPosition = MutableStateFlow(0f)
    val currentMediaPosition = _currentMediaPosition.asStateFlow()

    private var _currentMediaPositionInList = MutableStateFlow(0f)
    val currentMediaPositionInList = _currentMediaPositionInList.asStateFlow()


    private var _isPlayerPlaying = MutableStateFlow(false)
    val isPlayerPlaying = _isPlayerPlaying.asStateFlow()

    private var _isPlayerBuffering = MutableStateFlow(false)
    val isPlayerBuffering = _isPlayerBuffering.asStateFlow()

    private var _isShuffleClicked = MutableStateFlow(false)
    val isShuffleClicked = _isShuffleClicked.asStateFlow()

    private var _isRepeatClick = MutableStateFlow(false)
    val isRepeatClick = _isRepeatClick.asStateFlow()

    private var _currentEpisodeDurationInMinutes = MutableStateFlow(0L)
    val currentEpisodeDurationInMinutes = _currentEpisodeDurationInMinutes.asStateFlow()

    private var _currentEpisodeProgressInMinutes = MutableStateFlow(0L)
    val currentEpisodeProgressInMinutes = _currentEpisodeProgressInMinutes.asStateFlow()


    private val playerController =
        PlayerController(
            player,
            _currentEpisode,
            _currentMediaPosition,
            _currentEpisodeDurationInMinutes,
            _currentEpisodeProgressInMinutes,
            _isPlayerPlaying,
            _isPlayerBuffering,
            _isShuffleClicked,
            _isRepeatClick,
            viewModelScope,
            sharedPreferences,
            _currentMediaPositionInList
        )

    init {
        player.addListener(playerController)
        playerController.setupMediaNotification(context)
    }

    fun onPlayerEvents(event: PlayerEvents) {
        when (event) {
            is PlayerEvents.PausePlay -> playerController.pauseOrPlay()
            is PlayerEvents.GoToSpecificItem -> playerController.goToSpecificItem(event.index)
            is PlayerEvents.Next -> playerController.nextItem()
            is PlayerEvents.Previous -> playerController.previousItem()
            is PlayerEvents.Shuffle -> playerController.shuffleClick()
            is PlayerEvents.Repeat -> playerController.repeatClick()
            is PlayerEvents.AddPlaylist -> playerController.addPlaylist(
                event.episodes,
                event.isUpdatePlaylistRequired
            )
            is PlayerEvents.PlayNewEpisode -> playerController.playNewEpisode(event.episode)

            is PlayerEvents.SeekForward -> playerController.seekForward()
            is PlayerEvents.SeekBack -> playerController.seekBack()
            is PlayerEvents.MoveToSpecificPosition -> playerController.moveToSpecificPosition(event.position)
            is PlayerEvents.ClearMediaItems -> playerController.clearPlayer()
            is PlayerEvents.GetThePositionOfSpecificEpisodeInsideThePlaylist -> playerController.findTrackIndexById(
                event.id
            )

            is PlayerEvents.AddEpisodeToPlayNext -> playerController.setEpisodeToPlayNext(event.id)
        }
    }
}

