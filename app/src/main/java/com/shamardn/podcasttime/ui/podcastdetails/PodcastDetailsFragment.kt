package com.shamardn.podcasttime.ui.podcastdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.shamardn.podcasttime.data.remote.ApiService
import com.shamardn.podcasttime.databinding.FragmentPodcastDetailsBinding
import com.shamardn.podcasttime.domain.entity.Episode
import com.shamardn.podcasttime.util.changeDateFormat
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PodcastDetailsFragment : Fragment(), PodcastDetailsInteractionListener {
    private lateinit var binding: FragmentPodcastDetailsBinding
    private lateinit var podcastDetailsAdapter: PodcastDetailsAdapter
    private val navArgs: PodcastDetailsFragmentArgs by navArgs()
    private var items = mutableListOf<Episode>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPodcastDetailsBinding.inflate(inflater, container, false)

        fetchPodcastEpisodesById(navArgs.trackId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgPodcastDetailsBackArrow.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    private fun fetchPodcastEpisodesById(id: Int) {
        lifecycleScope.launch {
            withContext(lifecycleScope.coroutineContext) {
                val service = ApiService.instance
                items.clear()
                service.getPodcastById(id).results.forEach {
                    items.add(it)
                }
            }
            podcastDetailsAdapter = PodcastDetailsAdapter(items, this@PodcastDetailsFragment)
            binding.recyclerViewPodcastDetails.adapter = podcastDetailsAdapter
            binding.textPodcastDetailsArtistName.text = items[0].artistName
            binding.textPodcastDetailsCollectionName.text = items[0].collectionName
            binding.textAppbarTitle.text = items[0].collectionName
            binding.textPodcastDetailsGenreName.text = items[0].primaryGenreName
            binding.textPodcastDetailsDate.text = items[0].releaseDate.changeDateFormat()
            binding.textPodcastDetailsDesc.text = items[1].description
            binding.textPodcastDetailsEpisodesCount.text = "${items[0].trackCount} Episodes"
            Glide.with(binding.imgPodcastDetails).load(items[0].artworkUrl100).into(binding.imgPodcastDetails)

            Log.i("PodcastDetailsFragment", "${items[0]}")
        }
    }

    private fun showEpisodeDetailsBottomSheet(episodeUrl: String, artworkUrl: String, podcastTitle: String, episode: String) {
        val action = PodcastDetailsFragmentDirections.actionPodcastDetailsFragmentToEpisodeDetailsBottomSheet(episodeUrl, artworkUrl, podcastTitle, episode)
        Navigation.findNavController(binding.root).navigate(action)
    }

    override fun onClickEpisode(episodeUrl: String, artworkUrl: String, podcastTitle: String, episode: String) {
        showEpisodeDetailsBottomSheet(episodeUrl, artworkUrl, podcastTitle, episode)
    }
}