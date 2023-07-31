package com.shamardn.podcasttime.media.exoplayer

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.data.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.domain.usecase.GetDownloadedEpisodesUseCase
import com.shamardn.podcasttime.util.Constants.MEDIA_ROOT_ID
import com.shamardn.podcasttime.util.Resource
import com.shamardn.podcasttime.util.asAudio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val mediaServiceConnection: MediaServiceConnection,
    private val getDownloadedEpisodesUseCase: GetDownloadedEpisodesUseCase,
) : ViewModel() {
    private val _mediaItems = MutableLiveData<Resource<List<EpisodeEntity>>>()
    val mediaItems: MutableLiveData<Resource<List<EpisodeEntity>>>
        get() = _mediaItems

    val isConnected = mediaServiceConnection.isConnected
    val netWorkError = mediaServiceConnection.networkError
    val curPlayingAudio = mediaServiceConnection.curPlayingAudio
    val playbackState = mediaServiceConnection.playbackState
    val audioDuration = mediaServiceConnection.audioDuration

    init {
        _mediaItems.postValue(Resource.loading(null))
        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) { return@async getDownloadedEpisodesUseCase() }
            mediaServiceConnection.setList(deferred.await())
            mediaServiceConnection.subscribe(
                MEDIA_ROOT_ID,
                object : MediaBrowserCompat.SubscriptionCallback() {
                    override fun onChildrenLoaded(
                        parentId: String,
                        children: MutableList<MediaBrowserCompat.MediaItem>
                    ) {
                        super.onChildrenLoaded(parentId, children)
                        _mediaItems.postValue(Resource.success(children.asAudio()))
                    }
                }
            )
        }
    }

    fun fastForward(seconds: Int = 10) = viewModelScope.launch {
        mediaServiceConnection.fastForward(seconds)
    }

    fun rewind(seconds: Int = 10) = viewModelScope.launch {
        mediaServiceConnection.rewind(seconds)
    }

    fun skipToNextAudio() = viewModelScope.launch {
        mediaServiceConnection.skipToNext()
    }

    fun skipToPreviousAudio() = viewModelScope.launch {
        mediaServiceConnection.skipToPrevious()
    }

    fun playOrPauseAudio() = viewModelScope.launch {
        val state = mediaServiceConnection.mediaControllerCompat.playbackState.state
        Log.i("MediaViewModel", state.toString())

        if(state == PlaybackStateCompat.STATE_PLAYING)
            mediaServiceConnection.pause()
        else mediaServiceConnection.play()
    }

    fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) = viewModelScope.launch {
        mediaServiceConnection.playFromMediaId(mediaId, extras)
    }

    fun seekTo(pos: Long) = viewModelScope.launch {
        mediaServiceConnection.seekTo(pos)
    }

    fun stopPlayBack() {
        mediaServiceConnection.stop()
    }

    fun refresh() {
        mediaServiceConnection.refreshMediaBrowserChildren()
    }

    override fun onCleared() {
        super.onCleared()
        mediaServiceConnection.unSubscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {
            })
    }
}
