package com.shamardn.podcasttime.ui

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.databinding.FragmentBottomPlayerBinding
import com.shamardn.podcasttime.media.exoplayer.MediaViewModel
import com.shamardn.podcasttime.util.execute
import com.shamardn.podcasttime.util.setAlphaAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomPlayerFragment : Fragment() {

    private lateinit var binding: FragmentBottomPlayerBinding

    private val mediaViewModel: MediaViewModel by activityViewModels()

    private val sharedDataViewModel: SharedDataViewModel by activityViewModels()

    private var isUpdateSeeBar = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        binding = FragmentBottomPlayerBinding.inflate(inflater, container, false)

        Log.i("BottomPlayer", "onCreateView")
        sharedDataViewModel.episode.observe(viewLifecycleOwner) { episode ->
            if(episode != null){
                Log.i("BottomPlayer", episode.toString())
                Glide.with(binding.imgMainPlayer).load(episode.artworkUrl160)
                    .into(binding.imgMainPlayer)

                binding.textMainPlayerEpisodeTitle.text = episode.trackName
                Log.i("BottomPlayer art 160", episode.artworkUrl160)
            }
        }

        binding.mainProgress.visibility = View.VISIBLE

        mediaViewModel.mediaItems.observe(viewLifecycleOwner) {
            Log.i("BottomPlayerFragment", it.data.toString())
        }
        setViewContent()
        controllerMediaPlayer()
        setUpSeekBar()

        return binding.root
    }


    override fun onStart() {
        super.onStart()
        Log.i("BottomPlayer", "onViewStart")
        sharedDataViewModel.episode.observe(viewLifecycleOwner) { episode ->
            if(episode != null){
                Log.i("BottomPlayer", episode.toString())
                Glide.with(binding.imgMainPlayer).load(episode.artworkUrl160)
                    .into(binding.imgMainPlayer)
                Log.i("BottomPlayer art 160", episode.artworkUrl160)
            }
        }

        binding.mainProgress.visibility = View.VISIBLE
    }

        private fun setUpSeekBar() {
        binding.mainSeekBar.apply {
            mediaViewModel.audioDuration.observe(viewLifecycleOwner) { duration ->
                if (isUpdateSeeBar) duration?.let {
                    max = it
                }
            }
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    /**
                     * Y can set text time of song
                     */
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    isUpdateSeeBar = false
                }
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.let {
                        mediaViewModel.seekTo(it.progress.toLong())
                        isUpdateSeeBar = true
                    }
                }
            })
        }
    }

    private fun controllerMediaPlayer() =
        mediaViewModel.apply {
//            imgSkipPrevious.setOnClickListener {
//                it.setAlphaAnimation()
//                skipToPreviousAudio()
//            }
//            imgSkipNext.setOnClickListener {
//                it.setAlphaAnimation()
//                skipToNextAudio()
//            }
            binding.imgMainPlayerPlay.setOnClickListener {
                Toast.makeText(requireContext(), "clicked",Toast.LENGTH_SHORT).show()
                it.setAlphaAnimation()
                playOrPauseAudio()
            }

        }

    private fun setViewContent() {
        mediaViewModel.apply {
            mediaItems.observe(viewLifecycleOwner) { resources ->
                resources.execute(
                    onSuccess = {
                        binding.mainProgress.visibility = View.GONE
                    },
                    onError = { message ->
                        binding.mainProgress.visibility = View.GONE
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    },
                    onLoading = {
                        binding.mainProgress.visibility = View.VISIBLE
                    }
                )
            }

            curPlayingAudio.observe(viewLifecycleOwner) { metadataItemSong ->
                binding.textMainPlayerEpisodeTitle.text = metadataItemSong?.description?.title
            }

            playbackState.observe(viewLifecycleOwner) { state ->
                if (state?.state == PlaybackStateCompat.STATE_PLAYING)
                    binding.imgMainPlayerPlay.setBackgroundResource(R.drawable.ic_pause)
                else binding.imgMainPlayerPlay.setBackgroundResource(R.drawable.ic_play)
                state?.position?.toInt()?.let { statePos ->
                    binding.mainSeekBar.progress = statePos
            }
        }

    }
}

}