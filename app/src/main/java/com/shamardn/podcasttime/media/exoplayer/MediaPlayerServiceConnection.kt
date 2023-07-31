package com.shamardn.podcasttime.media.exoplayer

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shamardn.podcasttime.data.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.services.MediaPlaybackService
import com.shamardn.podcasttime.util.Constants
import com.shamardn.podcasttime.util.Constants.ConstantBundle.BUNDLE_DURATION
import com.shamardn.podcasttime.util.Constants.DURATION
import com.shamardn.podcasttime.util.Constants.NETWORK_ERROR
import com.shamardn.podcasttime.util.Event
import com.shamardn.podcasttime.util.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class MediaServiceConnection @Inject constructor(
    @ApplicationContext context: Context,
) {

    private val _playbackState = MutableLiveData<PlaybackStateCompat?>()
    val playbackState: LiveData<PlaybackStateCompat?>
        get() = _playbackState

    private val _isConnected = MutableLiveData<Event<Resource<Boolean>>>()
    val isConnected: LiveData<Event<Resource<Boolean>>>
        get() = _isConnected

    private val _curPlayingAudio = MutableLiveData<MediaMetadataCompat?>()
    val curPlayingAudio: LiveData<MediaMetadataCompat?>
        get() = _curPlayingAudio

    lateinit var mediaControllerCompat: MediaControllerCompat

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)

    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(
            context,
            MediaPlaybackService::class.java
        ),
        mediaBrowserConnectionCallback,
        null
    ).apply {
        connect()
    }

    private var audioList = mutableListOf<EpisodeEntity>()

    private val transportControls: MediaControllerCompat.TransportControls
        get() = mediaControllerCompat.transportControls

    private val _networkError = MutableLiveData<Event<Resource<Boolean>>>()
    val networkError: LiveData<Event<Resource<Boolean>>>
        get() = _networkError

    private val _audioDuration = MutableLiveData<Int?>()
    val audioDuration: LiveData<Int?>
        get() = _audioDuration

    val rootMediaId: String
        get() = mediaBrowser.root

    fun playAudio(audios: List<EpisodeEntity>) {
        audioList = audios as MutableList<EpisodeEntity>
        mediaBrowser.sendCustomAction(Constants.START_MEDIA_PLAY_ACTION, null, null)
    }

    fun fastForward(seconds: Int = 10) {
        _playbackState.value?.position?.let {
            transportControls.seekTo(it + seconds * 1000)
        }
    }

    fun rewind(seconds: Int = 10) {
        _playbackState.value?.position?.let {
            transportControls.seekTo(it - seconds * 1000)
        }
    }

    fun pause() {
        transportControls.pause()
    }

    fun play() {
        transportControls.play()
    }

    fun playFromMediaId(mediaId: String?, extras: Bundle?) {
        transportControls.playFromMediaId(mediaId,extras)
    }

    fun skipToNext() {
        transportControls.skipToNext()
    }

    fun skipToPrevious() {
        transportControls.skipToPrevious()
    }

    fun seekTo(pos: Long) {
        transportControls.seekTo(pos)
    }

    fun stop() {
        transportControls.stop()
    }

    fun setList(mList: List<EpisodeEntity>) {
        audioList.apply {
            clear()
            addAll(mList)
        }
    }

    fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(parentId,callback)
    }

    fun unSubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }

    private inner class MediaBrowserConnectionCallback(private val context: Context) :
        MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            super.onConnected()
            _isConnected.postValue(Event(Resource.success(true)))
            mediaControllerCompat = MediaControllerCompat(
                context,
                mediaBrowser.sessionToken
            ).apply {
                registerCallback(MediaControllerCallback())
            }
        }

        override fun onConnectionFailed() {
            super.onConnectionFailed()
            _isConnected.postValue(
                Event(
                    Resource.error(
                        "Couldn't connect to media browser", false
                    )
                )
            )
        }

        override fun onConnectionSuspended() {
            super.onConnectionSuspended()
            _isConnected.postValue(
                Event(
                    Resource.error(
                        "The connection was suspended", false
                    )
                )
            )
        }
    }

    fun refreshMediaBrowserChildren() {
        mediaBrowser.sendCustomAction(
            Constants.REFRESH_MEDIA_PLAY_ACTION,
            null,
            null
        )
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            _playbackState.postValue(state)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            _curPlayingAudio.postValue(metadata)
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)
            when (event) {
                NETWORK_ERROR -> _isConnected.postValue(
                    Event(
                        Resource.error(
                            "Couldn't connect to the server. Please check your internet connection.",
                            null
                        )))
                DURATION -> _audioDuration.postValue(extras?.getInt(BUNDLE_DURATION))
            }
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
            mediaBrowserConnectionCallback.onConnectionSuspended()
        }
    }
}


