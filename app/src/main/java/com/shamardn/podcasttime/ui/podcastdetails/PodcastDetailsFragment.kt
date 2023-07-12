package com.shamardn.podcasttime.ui.podcastdetails

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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.shamardn.podcasttime.databinding.FragmentPodcastDetailsBinding
import com.shamardn.podcasttime.domain.entity.EpisodeDTO
import com.shamardn.podcasttime.util.FileUtils
import com.shamardn.podcasttime.util.FileUtils.downloadMp3UsingUrl
import com.shamardn.podcasttime.util.changeDateFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PodcastDetailsFragment : Fragment(), PodcastDetailsInteractionListener {
    private lateinit var binding: FragmentPodcastDetailsBinding
    private lateinit var podcastDetailsAdapter: PodcastDetailsAdapter
    private val navArgs: PodcastDetailsFragmentArgs by navArgs()
    private val viewModel: PodcastDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPodcastDetailsBinding.inflate(inflater, container, false)
        viewModel.getPodcastById(navArgs.trackId)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchPodcastEpisodesById()
        binding.imgPodcastDetailsBackArrow.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    private fun fetchPodcastEpisodesById() {
        lifecycleScope.launch {
            viewModel.episodes.collect {
                if (it != null) {
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
                    Log.i("PodcastDetailsFragment", "${it.results[0]}")
                }
            }
        }
    }

    private fun showEpisodeDetailsBottomSheet(
        episodeUrl: String,
        artworkUrl: String,
        podcastTitle: String,
        episode: String,
        guid: String,
        episodeFileExtension: String,
    ) {
        val action =
            PodcastDetailsFragmentDirections.actionPodcastDetailsFragmentToEpisodeDetailsBottomSheet(
                episodeUrl,
                artworkUrl,
                podcastTitle,
                episode,
                guid,
                episodeFileExtension,
            )
        this.findNavController().navigate(action)
    }

    override fun onClickEpisode(
        episodeUrl: String,
        artworkUrl: String,
        podcastTitle: String,
        episode: String,
        guid: String,
        episodeFileExtension: String,
    ) {
        showEpisodeDetailsBottomSheet(episodeUrl, artworkUrl, podcastTitle, episode, guid, episodeFileExtension)
    }

    override fun onClickDownload(episodeDTO: EpisodeDTO) {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.saveEpisodeToDownload(episodeDTO)

            downloadMp3UsingUrl(
                requireContext(),
                episodeDTO.episodeUrl,
                FileUtils.getRootDirPath(requireContext()),
                "${episodeDTO.episodeGuid}.${episodeDTO.episodeFileExtension}"
            )
        }
    }


}