package com.shamardn.podcasttime.ui.episodeplayer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.Util
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shamardn.podcasttime.databinding.EpisodeBottomSheetLayoutBinding
import com.shamardn.podcasttime.util.FileUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EpisodeDetailsBottomSheet : BottomSheetDialogFragment() {
    private val navArgs: EpisodeDetailsBottomSheetArgs by navArgs()
    lateinit var binding: EpisodeBottomSheetLayoutBinding
    private val viewModel: EpisodePlayerViewModel by viewModels()

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var playerView: StyledPlayerView
    private lateinit var mediaItem: MediaItem

    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var isPlayerPlaying = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EpisodeBottomSheetLayoutBinding.inflate(inflater, container, false)
        viewModel.getEpisodeByGuid(navArgs.guid)

        if (navArgs.isOnline) {
            playEpisode()
            Log.i("BottomSheet","online")
        }else{
            playDownloadedEpisode()
            Log.i("BottomSheet","offline")
        }
//        getEpisode()

        if (savedInstanceState != null) {
            currentWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW)
            playbackPosition = savedInstanceState.getLong(STATE_RESUME_POSITION)
            isPlayerPlaying = savedInstanceState.getBoolean(STATE_PLAYER_PLAYING)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerView = binding.playerView
        Glide.with(binding.root.context).load(navArgs.artworkUrl).into(binding.imgEpisode)
        binding.textPodcast.text = navArgs.podcastTitle
        binding.textEpisode.text = navArgs.episode
    }

    private fun playEpisode() {
        mediaItem = MediaItem.Builder()
            .setUri(navArgs.episodeUrl)
            .build()
    }

    private fun playDownloadedEpisode() {
        val path =
            "${FileUtils.getRootDirPath(requireContext())}/${navArgs.guid}.${navArgs.episodeFileExtension}"
        mediaItem = MediaItem.fromUri(path)
    }

    fun getEpisode() {
        lifecycleScope.launch {
            try {
                viewModel.episode.collect { episode ->
                    if (episode != null) {
                        Glide.with(binding.root.context).load(episode.artworkUrl160)
                            .into(binding.imgEpisode)
                        binding.textPodcast.text = episode.collectionName
                        binding.textEpisode.text = episode.trackName

                    } else {
                        Log.i("BottomSheet", "null")
                    }
                }
            } catch (e: Exception) {
                Log.e("BottomSheet", e.message.toString())
            }
        }
    }

    private fun initPlayer() {
        exoPlayer = ExoPlayer.Builder(requireContext()).build().apply {
            playWhenReady = isPlayerPlaying
            seekTo(currentWindow, playbackPosition)
            setMediaItem(mediaItem, false)
            prepare()
        }
        playerView.player = exoPlayer
        playerView.showController()
    }

    private fun releasePlayer() {
        isPlayerPlaying = exoPlayer.playWhenReady
        playbackPosition = exoPlayer.currentPosition
        currentWindow = exoPlayer.currentMediaItemIndex
        exoPlayer.release()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_RESUME_WINDOW, exoPlayer.currentMediaItemIndex)
        outState.putLong(STATE_RESUME_POSITION, exoPlayer.currentPosition)
        outState.putBoolean(STATE_PLAYER_PLAYING, isPlayerPlaying)
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {

            initPlayer()
            playerView.onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23) {
            initPlayer()
            playerView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            playerView.onPause()
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            playerView.onPause()
            releasePlayer()
        }
    }

    companion object {
        const val STATE_RESUME_WINDOW = "resumeWindow"
        const val STATE_RESUME_POSITION = "resumePosition"
        const val STATE_PLAYER_PLAYING = "playerOnPlay"
    }
}