package com.shamardn.podcasttime.ui.podcastdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.data.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.databinding.FragmentPodcastDetailsBinding
import com.shamardn.podcasttime.domain.entity.EpisodeDTO
import com.shamardn.podcasttime.domain.entity.PodcastResponse
import com.shamardn.podcasttime.media.exoplayer.MediaViewModel
import com.shamardn.podcasttime.ui.main.MainActivity
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
    private val viewModel: PodcastDetailsViewModel by viewModels()
    private var podcastEntity: PodcastEntity? = null


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

        handelEvents()

        showBottomSheet()
    }

    private fun handelEvents() {
        binding.imgPodcastDetailsBackArrow.setOnClickListener {
            it.findNavController().popBackStack()
        }
        binding.imgPodcastDetailsSubscription.setOnClickListener {
            lifecycleScope.launch {
                podcastEntity?.let { podcast -> viewModel.onSubscribe(podcast) }
            }
            Toast.makeText(requireContext(),getString(R.string.subscription_completed),Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchPodcastEpisodesByIdForMedia() {
        lifecycleScope.launch {
            mediaViewModel.
            episodes.collect {
                if (it != null) {

                    podcastEntity = getPodcastEntity(it)

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
                    binding.textPodcastDetailsEpisodesCount.text = binding.root.resources.getString(R.string.trackCount, it.results[0].trackCount.toString())
                    Glide.with(binding.imgPodcastDetails).load(it.results[0].artworkUrl100)
                        .into(binding.imgPodcastDetails)
                }
            }
        }
    }

    private fun getPodcastEntity(podcast: PodcastResponse<EpisodeDTO>): PodcastEntity {
        return PodcastEntity(
            navArgs.trackId,
            podcast.results[0].artistName,
            podcast.results[0].collectionName,
            podcast.results[0].artworkUrl100,
            podcast.results[0].primaryGenreName,
            podcast.results[0].releaseDate,
            podcast.resultCount,
            podcast.results[0].trackName,
        )
    }

    override fun onClickEpisode(currentEpisode: EpisodeDTO) {
        mediaViewModel.playAudio(currentEpisode)
    }

    override fun onClickDownload(episodeDTO: EpisodeDTO) {
        lifecycleScope.launch(Dispatchers.Main) {
            mediaViewModel.saveEpisodeToDownload(episodeDTO)
            Toast.makeText(requireContext(),"Audio ${episodeDTO.trackName} is Downloaded", Toast.LENGTH_SHORT).show()
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