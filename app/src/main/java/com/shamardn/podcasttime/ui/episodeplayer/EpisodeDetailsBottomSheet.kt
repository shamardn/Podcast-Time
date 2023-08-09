package com.shamardn.podcasttime.ui.episodeplayer

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.databinding.EpisodeBottomSheetLayoutBinding
import com.shamardn.podcasttime.media.exoplayer.MediaViewModel
import com.shamardn.podcasttime.util.setAlphaAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EpisodeDetailsBottomSheet : BottomSheetDialogFragment() {
    lateinit var binding: EpisodeBottomSheetLayoutBinding
    private val mediaViewModel: MediaViewModel by activityViewModels()

    private var isUpdateSeeBar = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = EpisodeBottomSheetLayoutBinding.inflate(inflater, container, false)

        mediaViewModel.onBottomPlayerClick(false)

        setViewContent()
        setUpSeekBar()
        controllerMediaPlayer()
        return binding.root
    }

    fun setViewContent() {
        mediaViewModel.currentPlayingAudio.observe(viewLifecycleOwner) { episode ->
            Log.i("MediaVM", "episode observe: ${episode}")
            Glide.with(binding.imgBottomSheetEpisodeImg).load(episode?.artworkUrl160)
                .into(binding.imgBottomSheetEpisodeImg)
            binding.textBottomSheetPodcastTitle.text = episode?.trackName
            binding.textBottomSheetEpisodeTitle.text = episode?.collectionName
            binding.seekBarBottomSheet.max = (episode?.trackTimeMillis?.div(60000)!!)
        }

        mediaViewModel.apply {
            lifecycleScope.launch {
                playbackState.collect { state ->
                    if (state?.state == PlaybackStateCompat.STATE_PLAYING)
                        binding.imgBottomSheetPlayPause.setBackgroundResource(R.drawable.ic_pause)
                    else binding.imgBottomSheetPlayPause.setBackgroundResource(R.drawable.ic_play)
                }
            }
        }
    }

    private fun setUpSeekBar() {
        binding.seekBarBottomSheet.apply {
            if (isUpdateSeeBar) {
                max = (mediaViewModel.currentDuration / 60000).toInt()
            }

            lifecycleScope.launch {
               mediaViewModel.playbackState.collect { state ->
                    state?.position?.let { pos ->
                        binding.seekBarBottomSheet.progress = (pos / 60000).toInt()
                        Log.i("seekbarBottom", "state: ${state}")
                    }
                }
            }

            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean,
                ) {
                    /**
                     * Y can set text time of song
                     */
                    Log.i("seekbarBottom", "progress: ${progress}")


                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    isUpdateSeeBar = false
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.let {
                        mediaViewModel.seekTo(it.progress.toFloat())
                        isUpdateSeeBar = true
                    }
                }
            })
        }
    }

    private fun controllerMediaPlayer() =
        mediaViewModel.apply {
            binding.imgBottomSheetFastForward.setOnClickListener {
                it.setAlphaAnimation()
                fastForward()
            }
            binding.imgBottomSheetFastRewind.setOnClickListener {
                it.setAlphaAnimation()
                rewind()
            }
            binding.imgBottomSheetPlayPause.setOnClickListener {
                it.setAlphaAnimation()
                onBottomPlayerClickPlayPause()
            }
        }
}