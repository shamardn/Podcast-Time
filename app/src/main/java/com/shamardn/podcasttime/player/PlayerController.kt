package com.shamardn.podcasttime.player

import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.shamardn.podcasttime.data.model.Resource
import com.shamardn.podcasttime.domain.mapper.toEpisodeEntity
import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState
import com.shamardn.podcasttime.util.Constants.LAST_PLAYED_EPISODE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PlayerController(
    private val player: ExoPlayer,
    private var currentEpisode: MutableStateFlow<Resource<EpisodeUiState>>,
    private var currentMediaPosition: MutableStateFlow<Float>,
    private var currentMediaDurationInMinutes: MutableStateFlow<Long>,
    private var currentMediaProgressInMinutes: MutableStateFlow<Long>,
    private var isPausePlayClicked: MutableStateFlow<Boolean>,
    private var isPlayerBuffering: MutableStateFlow<Boolean>,
    private var isShuffleClicked: MutableStateFlow<Boolean>,
    private var isRepeatClicked: MutableStateFlow<Boolean>,
    private val viewModelScope: CoroutineScope,
    private var sharedPreferences: SharedPreferences,
    private var currentMediaPositionInList: MutableStateFlow<Float>,
) : Player.Listener {

    var duration: Long = 0

    private lateinit var controller: ListenableFuture<MediaController>
    private var episodeIdToPlayNext: String = ""

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        currentMediaPosition.value = 0f

        if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO && episodeIdToPlayNext != "") {
            findTrackIndexById(episodeIdToPlayNext)
            episodeIdToPlayNext = ""
        }
        if (mediaItem != null) {
            viewModelScope.launch {
                currentEpisode.emit(Resource.Success(toEpisodeEntity(mediaItem)))
                saveFloatValue(player.currentMediaItemIndex.toFloat())
                currentMediaPositionInList.value = player.currentMediaItemIndex.toFloat()
            }
        }
    }

    fun findTrackIndexById(trackId: String): Int {
        for (i in 0 until player.mediaItemCount) {
            val mediaItem = player.getMediaItemAt(i)
            if (mediaItem.mediaId == trackId) {
                goToSpecificItem(i)
                return i
            }
        }
        return -1 // Track not found
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        isPausePlayClicked.value = isPlaying
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)

        when (playbackState) {
            Player.STATE_ENDED -> {
                if (player.hasNextMediaItem()) {
                    nextItem()
                    saveFloatValue(player.currentMediaItemIndex.toFloat())
                }
            }

            Player.STATE_BUFFERING -> {
                isPlayerBuffering.value = true
            }

            Player.STATE_IDLE -> {
                currentMediaProgressInMinutes.value = 0L
                currentMediaDurationInMinutes.value = 0L
                isPlayerBuffering.value = false
            }

            Player.STATE_READY -> {
                isPlayerBuffering.value = false
            }
        }
    }


    fun pauseOrPlay() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
        viewModelScope.launch {
            currentEpisode.emit(Resource.Success(toEpisodeEntity(player.currentMediaItem!!)))
        }
    }

    fun shuffleClick() {
        if (isShuffleClicked.value) {
            isShuffleClicked.value = false
            player.shuffleModeEnabled = isShuffleClicked.value
        } else {
            isShuffleClicked.value = true
            player.shuffleModeEnabled = isShuffleClicked.value
        }
    }


    fun repeatClick() {
        if (isRepeatClicked.value) {
            isRepeatClicked.value = false
            player.repeatMode = Player.REPEAT_MODE_OFF
        } else {
            isRepeatClicked.value = true
            player.repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    fun seekForward() {
        player.seekForward()
    }

    fun seekBack() {
        player.seekBack()
    }


    fun clearPlayer() {
        player.stop()
        player.clearMediaItems()
    }

    fun addPlaylist(itemList: List<EpisodeUiState>, updatePlaylistRequired: Boolean) {
        if (updatePlaylistRequired) {
            for (item in itemList) {
                val metadata = getMetaDataFromItem(item)
                val mediaItem = MediaItem.Builder().apply {
                    setUri(item.episodeUrl)
                    setMediaId(item.guid)
                    setMediaMetadata(metadata)

                }.build()
                player.addMediaItem(mediaItem)
            }
            player.prepare()
            player.pause()
        } else {
            if (player.mediaItemCount <= 0) {
                for (item in itemList) {
                    val metadata = getMetaDataFromItem(item)
                    val mediaItem = MediaItem.Builder().apply {
                        setUri(item.episodeUrl)
                        setMediaId(item.guid)
                        setMediaMetadata(metadata)

                    }.build()
                    player.addMediaItem(mediaItem)
                }
                player.prepare()
                player.pause()
            }
        }
    }

    fun playNewEpisode(episode: EpisodeUiState) {
        val metadata = getMetaDataFromItem(episode)
        player.clearMediaItems()
        val mediaItem = MediaItem.Builder().apply {
            setUri(episode.episodeUrl)
            setMediaId(episode.guid)
            setMediaMetadata(metadata)

        }.build()
        player.addMediaItem(mediaItem)

        player.prepare()
        player.play()
    }

    fun nextItem() {
        if (player.hasNextMediaItem()) {
            player.seekToNextMediaItem()
            currentMediaPositionInList.value = player.currentMediaItemIndex.toFloat()
        }
    }

    fun goToSpecificItem(index: Int) {
        player.seekTo(index, 0L)
        player.play()
        viewModelScope.launch {
            currentEpisode.emit(Resource.Success(toEpisodeEntity(player.currentMediaItem!!)))
        }
        saveFloatValue(player.currentMediaItemIndex.toFloat())
        currentMediaPositionInList.value = player.currentMediaItemIndex.toFloat()
    }

    fun previousItem() {
        if (player.hasPreviousMediaItem()) {
            player.seekToPreviousMediaItem()
            currentMediaPositionInList.value = player.currentMediaItemIndex.toFloat()
        }
    }


    fun moveToSpecificPosition(position: Long) {
        player.seekTo(position)
    }

    private fun getMetaDataFromItem(item: EpisodeUiState): MediaMetadata {

        val extras = Bundle().apply {
            putString("KEY_EPISODE_PATH", item.episodeUrl)
        }

        return MediaMetadata.Builder()
            .setTitle(item.trackName)
            .setAlbumTitle(item.collectionName)
            .setDisplayTitle(item.trackName)
            .setArtist(item.collectionName)
            .setAlbumArtist(item.collectionName)
            .setArtworkUri(Uri.parse(item.artworkUrl600))
            .setExtras(extras) // Add extras bundle to metadata
            .build()
    }

    fun updatePlayerSeekProgress(pos: Long) {
        currentMediaProgressInMinutes.value = pos
        val progress = pos.toFloat() / duration.toFloat()
        if (!progress.isNaN()) currentMediaPosition.value = progress
    }

    fun setupMediaNotification(context: Context) {
        val sessionToken = SessionToken(context, ComponentName(context, MediaService::class.java))
        controller = MediaController.Builder(context, sessionToken).buildAsync()
        controller.addListener({

            val mediaController = controller.get()

            mediaController.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    viewModelScope.launch {
                        currentEpisode.emit(Resource.Success(toEpisodeEntity(player.currentMediaItem!!)))
                    }
//                    saveFloatValue(player.currentMediaItemIndex.toFloat())

                    isPausePlayClicked.value = isPlaying
                    duration = mediaController.duration
                    if (duration == -9223372036854775807) duration = 0
                    currentMediaDurationInMinutes.value = duration
                    viewModelScope.launch {
                        while (isPausePlayClicked.value) {
                            viewModelScope.launch {
                                currentEpisode.emit(Resource.Success(toEpisodeEntity(player.currentMediaItem!!)))
                            }
//                            saveFloatValue(player.currentMediaItemIndex.toFloat())
                            updatePlayerSeekProgress(player.currentPosition)
                            delay(1000)
                        }
                    }
                }


                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int,
                ) {
                    super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                    when (reason) {
                        Player.DISCONTINUITY_REASON_SEEK -> {
                            updatePlayerSeekProgress(newPosition.contentPositionMs)
                            player.seekTo(newPosition.contentPositionMs)
                        }

                        Player.DISCONTINUITY_REASON_AUTO_TRANSITION -> Unit
                        Player.DISCONTINUITY_REASON_INTERNAL -> Unit
                        Player.DISCONTINUITY_REASON_REMOVE -> Unit
                        Player.DISCONTINUITY_REASON_SEEK_ADJUSTMENT -> Unit
                        Player.DISCONTINUITY_REASON_SKIP -> Unit
                    }
                }
            })
        }, MoreExecutors.directExecutor())
    }

    private fun saveFloatValue(value: Float) {
        val editor = sharedPreferences.edit()
        editor.putFloat(LAST_PLAYED_EPISODE, value)
        editor.apply()
    }

    fun setEpisodeToPlayNext(songId: String) {
        episodeIdToPlayNext = songId
    }

}