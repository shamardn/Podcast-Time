package com.shamardn.podcasttime.media.exoplayer

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.shamardn.podcasttime.data.local.database.entity.EpisodeAudio
import com.shamardn.podcasttime.services.MediaPlaybackService
import com.shamardn.podcasttime.util.Constants.REFRESH_MEDIA_PLAY_ACTION
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MediaPlayerServiceConnection @Inject constructor(
    @ApplicationContext context: Context
) {

    private val _playBackState: MutableStateFlow<PlaybackStateCompat?> =
        MutableStateFlow(null)
    val plaBackState: StateFlow<PlaybackStateCompat?>
        get() = _playBackState

    private val _isConnected: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val isConnected: StateFlow<Boolean>
        get() = _isConnected

    val currentPlayingAudio = MutableLiveData<EpisodeAudio?>(null)

    lateinit var mediaControllerCompat: MediaControllerCompat

    private val mediaBrowserServiceCallback =
        MediaBrowserConnectionCallBack(context)
    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(context, MediaPlaybackService::class.java),
        mediaBrowserServiceCallback,
        null
    ).apply {
        Log.i("podcastTime connection", " inside MediaBrowserCompat ")

        connect()
    }
    private var audioList = mutableListOf<EpisodeAudio>()

    val rootMediaId: String
        get() = mediaBrowser.root

    val transportControl: MediaControllerCompat.TransportControls
        get() = mediaControllerCompat.transportControls


    fun playAudio(audios:List<EpisodeAudio>){
        Log.i("podcastTime connection", " inside playAudio() ")

        audioList = audios as MutableList<EpisodeAudio>
        mediaBrowser.sendCustomAction(REFRESH_MEDIA_PLAY_ACTION,null,null)
    }

    fun fastForward(seconds:Int = 10){
        plaBackState.value?.currentPosition?.let {
            transportControl.seekTo(it + seconds * 1000)
        }
    }

    fun rewind(seconds:Int = 10){
        plaBackState.value?.currentPosition?.let {
            transportControl.seekTo(it - seconds * 1000)
        }
    }

    fun skipToNext(){
        transportControl.skipToNext()
    }

    fun subscribe(
        parentId:String,
        callBack:MediaBrowserCompat.SubscriptionCallback
    ){
//
        Log.i("podcastTime connection", " inside subscribe() ")
//        val bundle = Bundle()
//        bundle.putParcelableArrayList(Constants.ConstantBundle.BUNDLE_COMMAND , audioList as ArrayList)
//        mediaBrowser.sendCustomAction(START_MEDIA_PLAY_ACTION , bundle , null)
//


        mediaBrowser.subscribe(parentId, callBack)
    }

    fun unSubscribe(
        parentId:String,
        callBack:MediaBrowserCompat.SubscriptionCallback
    ){
        Log.i("podcastTime connection", " inside unSubscribe() ")

        mediaBrowser.unsubscribe(parentId,callBack)
    }

    fun refreshMediaBrowserChildren(){
        Log.i("podcastTime connection", " inside refreshMediaBrowserChildren() ")

        mediaBrowser.sendCustomAction(
            REFRESH_MEDIA_PLAY_ACTION,
            null,
            null
        )
    }

    private inner class MediaBrowserConnectionCallBack(
        private val context: Context
    ) : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            Log.i("podcastTime connection", " inside MediaBrowserConnectionCallBack")

            _isConnected.value = true
            mediaControllerCompat = MediaControllerCompat(
                context,
                mediaBrowser.sessionToken
            ).apply {
                registerCallback(MediaControllerCallBack())
            }
        }

        override fun onConnectionSuspended() {
            Log.i("podcastTime connection", " inside onConnectionSuspended")

            _isConnected.value = false
        }

        override fun onConnectionFailed() {
            Log.i("podcastTime connection", " inside onConnectionFailed")

            _isConnected.value = false
        }
    }


    private inner class MediaControllerCallBack : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            Log.i("podcastTime connection", " inside onPlaybackStateChanged")

            _playBackState.value = state
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            Log.i("podcastTime connection", " inside onMetadataChanged")

            currentPlayingAudio.value = metadata?.let { data ->
                audioList.find {
                    it.id.toString() == data.description.mediaId
                }
            }
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
            Log.i("podcastTime connection", " inside onSessionDestroyed")

            mediaBrowserServiceCallback.onConnectionSuspended()
        }


    }

    fun setList(mList: List<EpisodeAudio>) {
        Log.i("podcastTime connection", " inside setList()")

        audioList.apply {
            clear()
            addAll(mList)
        }
    }
}