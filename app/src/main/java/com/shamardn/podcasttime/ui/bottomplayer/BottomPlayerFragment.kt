package com.shamardn.podcasttime.ui.bottomplayer

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.databinding.FragmentBottomPlayerBinding
import com.shamardn.podcasttime.media.exoplayer.MediaViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BottomPlayerFragment : Fragment() {

    private lateinit var binding: FragmentBottomPlayerBinding

    private val mediaViewModel: MediaViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBottomPlayerBinding.inflate(inflater, container, false)

        Log.i("BottomPlayer", "onCreateView")

        binding.imgMainPlayerPlay.setOnClickListener {
            mediaViewModel.onBottomPlayerClickPlayPause()
        }

        binding.root.setOnClickListener {
            mediaViewModel.onBottomPlayerClick(true)
        }

        setViewContent()

        return binding.root
    }

    private fun setViewContent() {
        binding.root.visibility = View.GONE
        mediaViewModel.apply {
            currentPlayingAudio.observe(viewLifecycleOwner) { episode ->
                if (episode != null) {
                    binding.root.visibility = View.VISIBLE
                }
                Log.i("BottomPlayerFragment", episode.toString())
                binding.textMainPlayerEpisodeTitle.text = episode?.trackName
                Glide.with(binding.imgMainPlayer).load(episode?.artworkUrl160)
                    .into(binding.imgMainPlayer)
            }

            lifecycleScope.launch {
                playbackState.collect { state ->
                    if (state?.state == PlaybackStateCompat.STATE_PLAYING)
                        binding.imgMainPlayerPlay.setBackgroundResource(R.drawable.ic_pause)
                    else binding.imgMainPlayerPlay.setBackgroundResource(R.drawable.ic_play)

                }
            }
        }
    }

}