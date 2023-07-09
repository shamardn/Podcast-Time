package com.shamardn.podcasttime.ui.download

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.shamardn.podcasttime.databinding.FragmentDownloadsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DownloadsFragment : Fragment(), DownloadsInteractionListener {

    lateinit var binding: FragmentDownloadsBinding
    private val viewModel: DownloadsViewModel by viewModels()
    lateinit var downloadAdapter: DownloadsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDownloadsBinding.inflate(inflater, container, false)

        viewModel.getDownloadedEpisodes()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDownloadedEpisodes()
        binding.imgDownloadsBackArrow.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    fun getDownloadedEpisodes() {
        lifecycleScope.launch {
            try {
                viewModel.episodes.collect{ episodes ->
                    if (episodes != null){
                        downloadAdapter = DownloadsAdapter(episodes,this@DownloadsFragment)
                        binding.recyclerDownloads.adapter = downloadAdapter
                    }
                }
            }catch (e: Exception){
                Log.e("DownloadsFragment", e.message.toString())
            }
        }
    }

    override fun onEpisodeClick(
        episodeUrl: String,
        artworkUrl: String,
        podcastTitle: String,
        episode: String,
    ) {
        showEpisodeDetailsBottomSheet(episodeUrl, artworkUrl, podcastTitle, episode)
    }

    private fun showEpisodeDetailsBottomSheet(episodeUrl: String, artworkUrl: String, podcastTitle: String, episode: String) {
        val action = DownloadsFragmentDirections.actionDownloadsFragmentToEpisodeDetailsBottomSheet(
                episodeUrl,
                artworkUrl,
                podcastTitle,
                episode
            )
        this.findNavController().navigate(action)
    }


    override fun onDownloadedEpisodeIcon() {

    }
}