package com.shamardn.podcasttime.ui.podcastdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.shamardn.podcasttime.databinding.FragmentPodcastDetailsBinding
import com.shamardn.podcasttime.domain.entity.EpisodeDTO
import com.shamardn.podcasttime.media.exoplayer.MediaViewModel
import com.shamardn.podcasttime.ui.main.MainActivity
import com.shamardn.podcasttime.util.FileUtils
import com.shamardn.podcasttime.util.FileUtils.downloadMp3UsingUrl
import com.shamardn.podcasttime.util.changeDateFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PodcastDetailsFragment: Fragment(), PodcastDetailsInteractionListener {
    private lateinit var binding: FragmentPodcastDetailsBinding
    private lateinit var podcastDetailsAdapter: PodcastDetailsAdapter
    private val navArgs: PodcastDetailsFragmentArgs by navArgs()
    private val mediaViewModel: MediaViewModel by activityViewModels()
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

        binding = FragmentPodcastDetailsBinding.inflate(inflater, container, false)

        mediaViewModel.getPodcastById(navArgs.trackId)
        setBottomNavigationVisibility()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchPodcastEpisodesByIdForMedia()

        binding.imgPodcastDetailsBackArrow.setOnClickListener {
            it.findNavController().popBackStack()
        }

        showBottomSheet()
    }

    private fun fetchPodcastEpisodesByIdForMedia() {
        lifecycleScope.launch {
            mediaViewModel.
            episodes.collect {
                if (it != null) {

                    mediaViewModel.saveAllEpisodes(it.results)

                    podcastDetailsAdapter =
                        PodcastDetailsAdapter(it.results, this@PodcastDetailsFragment)
                    binding.recyclerViewPodcastDetails.adapter = podcastDetailsAdapter
                    binding.textPodcastDetailsArtistName.text = it.results[0].artistName
                    binding.textPodcastDetailsCollectionName.text = it.results[0].collectionName
                    binding.textAppbarTitle.text = it.results[0].collectionName
                    binding.textPodcastDetailsGenreName.text = it.results[0].primaryGenreName
                    binding.textPodcastDetailsDate.text =
                        it.results[0].releaseDate.changeDateFormat()
                    binding.textPodcastDetailsDesc.text = it.results[1].description
                    binding.textPodcastDetailsEpisodesCount.text =
                        "${it.results[0].trackCount} Episodes"
                    Glide.with(binding.imgPodcastDetails).load(it.results[0].artworkUrl100)
                        .into(binding.imgPodcastDetails)

                }
            }
        }
    }

    override fun onClickEpisode(currentEpisode: EpisodeDTO) {
        mediaViewModel.playAudio(currentEpisode)
    }

    override fun onClickDownload(episodeDTO: EpisodeDTO) {
        lifecycleScope.launch(Dispatchers.IO) {
            mediaViewModel.saveEpisodeToDownload(episodeDTO)

            downloadMp3UsingUrl(
                requireContext(),
                episodeDTO.episodeUrl,
                FileUtils.getRootDirPath(requireContext()),
                "${episodeDTO.episodeGuid}.${episodeDTO.episodeFileExtension}"
            )
        }
    }

    private fun showBottomSheet() {
        mediaViewModel.isBottomSheetOpened.observe(viewLifecycleOwner) {
            if (it) {
                val action =
                    PodcastDetailsFragmentDirections.actionPodcastDetailsFragmentToEpisodeDetailsBottomSheet()
                this.findNavController().navigate(action)
            }
        }
    }

}