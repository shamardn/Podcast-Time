package com.shamardn.podcasttime.ui.bottomplayer

import android.os.Bundle
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
import com.shamardn.podcasttime.ui.common.viewmodel.PlayerViewModel
import com.shamardn.podcasttime.ui.episodeplayer.EpisodeDetailsBottomSheet
import com.shamardn.podcasttime.ui.main.MainActivity
import com.shamardn.podcasttime.util.PlayerEvents
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BottomPlayerFragment : Fragment() {

    private lateinit var binding: FragmentBottomPlayerBinding

    private val playerViewModel: PlayerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBottomPlayerBinding.inflate(inflater, container, false)

        Log.i("BottomPlayer", "onCreateView")

        setViewContent()

        handleEvents()

        return binding.root
    }

    private fun handleEvents() {
        binding.imgMainPlayerPlay.setOnClickListener {
            playerViewModel.onPlayerEvents(PlayerEvents.PausePlay)
        }

        lifecycleScope.launch(Main) {
            playerViewModel.currentEpisode.collect { resource ->
                val episode = resource.data ?: return@collect
                binding.root.setOnClickListener {
                    val episodeDetailsBottomSheet = EpisodeDetailsBottomSheet(episode)
                    childFragmentManager.let {
                        episodeDetailsBottomSheet.show(it, episodeDetailsBottomSheet.tag)
                    }
                }
            }
        }
    }

    private fun setBottomPlayerVisibility(visibility: Int) {
        if (activity is MainActivity) {
            (activity as MainActivity).setBottomPlayerVisibility(visibility)
        }
    }

    private fun setViewContent() {
        setBottomPlayerVisibility(View.GONE)
        lifecycleScope.launch(Main) {
            playerViewModel.currentEpisode.collect { resource ->
                val episode = resource.data ?: return@collect

                setBottomPlayerVisibility(View.VISIBLE)
                binding.textMainPlayerEpisodeTitle.text = episode.trackName
                Glide.with(binding.imgMainPlayer).load(episode.artworkUrl100)
                    .into(binding.imgMainPlayer)
            }
        }

        lifecycleScope.launch {
            playerViewModel.isPlayerPlaying.collect {
                if (it) binding.imgMainPlayerPlay.setBackgroundResource(R.drawable.ic_pause)
                else binding.imgMainPlayerPlay.setBackgroundResource(R.drawable.ic_play)
            }
        }
    }
}