package com.shamardn.podcasttime.ui.download

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.shamardn.podcasttime.data.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.databinding.FragmentDownloadsBinding
import com.shamardn.podcasttime.media.exoplayer.MediaViewModel
import com.shamardn.podcasttime.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DownloadsFragment : Fragment(), DownloadsInteractionListener {
    lateinit var binding: FragmentDownloadsBinding
    private val mediaViewModel: MediaViewModel by activityViewModels()
    private val viewModel: DownloadsViewModel by viewModels()
    lateinit var downloadAdapter: DownloadsAdapter
    private var bottomNavigationViewVisibility = View.GONE

    private fun setBottomNavigationVisibility() {
        if (activity is MainActivity) {
            (activity as MainActivity).setBottomNavigationVisibility(bottomNavigationViewVisibility)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDownloadsBinding.inflate(inflater, container, false)

        mediaViewModel.getDownloadedEpisodes()

        setBottomNavigationVisibility()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showDownloadedEpisodes()

        showBottomSheet()

        binding.imgDownloadsBackArrow.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    private fun showDownloadedEpisodes() {
        lifecycleScope.launch {
            try {
                mediaViewModel.downloadedEpisodes.collect { episodes ->
                    if (episodes != null) {
                        downloadAdapter = DownloadsAdapter(episodes, this@DownloadsFragment)
                        binding.recyclerDownloads.adapter = downloadAdapter
                    }
                }
            } catch (e: Exception) {
                Log.e("DownloadsFragment", e.message.toString())
            }
        }
    }

    override fun onEpisodeClick(currentEpisode: EpisodeEntity) {
        mediaViewModel.playDownloadedEpisode(currentEpisode)
    }

    override fun onDeleteEpisodeClick(currentEpisode: EpisodeEntity) {
        viewModel.deleteEpisode(currentEpisode)
        mediaViewModel.getDownloadedEpisodes()
        showDownloadedEpisodes()
    }

    private fun showBottomSheet() {
        mediaViewModel.isBottomSheetOpened.observe(viewLifecycleOwner) {
            if (it) {
                val action =
                    DownloadsFragmentDirections.actionDownloadsFragmentToEpisodeDetailsBottomSheet()
                this.findNavController().navigate(action)
            }
        }
    }
}