package com.shamardn.podcasttime.media.exoplayer

import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.domain.entity.EpisodeDTO
import com.shamardn.podcasttime.domain.entity.PodcastResponse
import com.shamardn.podcasttime.domain.mapper.EpisodeAudioMapper
import com.shamardn.podcasttime.domain.usecase.GetPodcastByIdUseCase
import com.shamardn.podcasttime.domain.usecase.SaveAllEpisodesUseCase
import com.shamardn.podcasttime.domain.usecase.SaveEpisodeToDownloadUseCase
import com.shamardn.podcasttime.services.MediaPlaybackService
import com.shamardn.podcasttime.util.Constants.MEDIA_ROOT_ID
import com.shamardn.podcasttime.util.Constants.PLAYBACK_UPDATE_INTERVAL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val audioMapper: EpisodeAudioMapper,
    private val getPodcastByIdUseCase: GetPodcastByIdUseCase,
    private val saveEpisodeToDownloadUseCase: SaveEpisodeToDownloadUseCase,
    private val saveAllEpisodesUseCase: SaveAllEpisodesUseCase,
    serviceConnection: MediaPlayerServiceConnection,
) : ViewModel() {

    private var _trackId = MutableLiveData<Long>()
    val trackId: LiveData<Long> = _trackId

    private var _isBottomSheetOpened = MutableLiveData(false)
    val isBottomSheetOpened: LiveData<Boolean> = _isBottomSheetOpened

    private val _episodes = MutableStateFlow<PodcastResponse<EpisodeDTO>?>(null)
    val episodes: StateFlow<PodcastResponse<EpisodeDTO>?> = _episodes

    val currentPlayingAudio = serviceConnection.currentPlayingAudio
    val isConnected = serviceConnection.isConnected
    lateinit var rootMediaId: String
    var currentPlayBackPosition = MutableLiveData<Long>()
    private var updatePosition = true
    val playbackState = serviceConnection.playBackState
    val isAudioPlaying: Boolean
        get() = playbackState.value?.isPlaying == true
    private val subscriptionCallback = object
        : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(
            parentId: String,
            children: MutableList<MediaBrowserCompat.MediaItem>,
        ) {
            Log.i("podcastTime MediaVM", " inside onChildrenLoaded() ")

            super.onChildrenLoaded(parentId, children)
        }
    }
    private val serviceConnection = serviceConnection.also {
        Log.i("podcastTime MediaVM", " inside serviceConnection ")

        updatePlayBack()
    }

    val currentDuration: Long
        get() = MediaPlaybackService.currentDuration

    var currentAudioProgress = MutableLiveData(0f)

    init {
        Log.i("podcastTime MediaVM", " inside MediaViewModelInit ")

        viewModelScope.launch {
            isConnected.collect {
                if (it) {
                    rootMediaId = serviceConnection.rootMediaId
                    serviceConnection.playBackState.value?.apply {
                        currentPlayBackPosition.postValue(position)
                    }
                    serviceConnection.subscribe(rootMediaId, subscriptionCallback)
                }
            }
        }
        Log.i("podcastTime MediaVM", " inside MediaViewModelInit end ")

    }

    fun getPodcastById(trackId: Long) {
        try {
            viewModelScope.launch {
                withContext(viewModelScope.coroutineContext) {
                    _episodes.value = getPodcastByIdUseCase(trackId)
                    _trackId.postValue(trackId)
                }
            }
        } catch (e: Exception) {
            Log.e("MediaViewModel", e.message.toString())
        }
    }


    suspend fun saveEpisodeToDownload(episodeDTO: EpisodeDTO) {
        try {
            viewModelScope.launch {
                saveEpisodeToDownloadUseCase(episodeDTO)
            }
        } catch (e: Exception) {
            Log.e("MediaViewModel", e.message.toString())
        }
    }

    suspend fun saveAllEpisodes(episodesDTO: List<EpisodeDTO>) {
        try {
            viewModelScope.launch {
                Log.i("podcastTime MediaVM", " inside saveAllEpisodes ")

                saveAllEpisodesUseCase(episodesDTO)
            }
        } catch (e: Exception) {
            Log.e("MediaViewModel", e.message.toString())
        }
    }

    fun playAudio(currentAudio: EpisodeDTO) {
        episodes.value?.results?.let { audioMapper.mapList(it) }
            ?.let { serviceConnection.playAudio(it) }

        if (audioMapper.map(currentAudio).id == currentPlayingAudio.value?.id) {
            if (isAudioPlaying) {
                serviceConnection.transportControl.pause()
            } else {
                serviceConnection.transportControl.play()
            }
        } else {
            serviceConnection.transportControl
                .playFromMediaId(
                    audioMapper.map(currentAudio).id.toString(),
                    null
                )
        }
        Log.i("podcastTime MediaVM", " inside playAudio end ")

    }

    fun stopPlayBack() {
        serviceConnection.transportControl.stop()
    }

    fun fastForward() {
        serviceConnection.fastForward()
    }

    fun rewind() {
        serviceConnection.rewind()
    }

    fun skipToNext() {
        serviceConnection.skipToNext()

    }

    fun skipToPrevious() {
        serviceConnection.skipToPrevious()
    }

    fun seekTo(value: Float) {
        serviceConnection.transportControl.seekTo(
            (currentDuration * value / 100f).toLong()
        )
    }

    private fun updatePlayBack() {
        Log.i("podcastTime MediaVM", " inside updatePlayBack start ")
        viewModelScope.launch {
            val position = playbackState.value?.currentPosition ?: 0

            if (currentPlayBackPosition.value != position) {
                currentPlayBackPosition.postValue(position)
            }

            if (currentDuration > 0) {
                currentAudioProgress.postValue(
                    (currentPlayBackPosition.value?.toFloat()
                        ?.div(currentDuration.toFloat()) ?: 0f) * 100f
                )
            }
            delay(PLAYBACK_UPDATE_INTERVAL)
            if (updatePosition) {
                updatePlayBack()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("podcastTime MediaVM", " inside onCleared  ")

        serviceConnection.unSubscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {}
        )
        updatePosition = false
    }

    fun onBottomPlayerClickPlayPause() {
        if(isAudioPlaying) {
            serviceConnection.transportControl.pause()
        } else {
            serviceConnection.transportControl.play()
        }
    }

    fun onBottomPlayerClick(isOpened: Boolean) {
        _isBottomSheetOpened.postValue(isOpened)
    }
}