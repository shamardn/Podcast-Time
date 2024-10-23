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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.data.datasource.local.database.entity.HistoryEntity
import com.shamardn.podcasttime.data.datasource.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.data.model.Resource
import com.shamardn.podcasttime.databinding.FragmentPodcastDetailsBinding
import com.shamardn.podcasttime.ui.common.custom_views.ProgressDialog
import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState
import com.shamardn.podcasttime.ui.common.viewmodel.PlayerViewModel
import com.shamardn.podcasttime.ui.episodeplayer.EpisodeDetailsBottomSheet
import com.shamardn.podcasttime.ui.showSnakeBarError
import com.shamardn.podcasttime.util.CrashlyticsUtils
import com.shamardn.podcasttime.util.DetailsException
import com.shamardn.podcasttime.util.PlayerEvents
import com.shamardn.podcasttime.util.changeDateFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PodcastDetailsFragment : Fragment(), PodcastDetailsInteractionListener {

    private var _binding: FragmentPodcastDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var podcastDetailsAdapter: PodcastDetailsAdapter
    private val navArgs: PodcastDetailsFragmentArgs by navArgs()
    private val playerViewModel: PlayerViewModel by activityViewModels()
    private val podcastDetailsViewModel: PodcastDetailsViewModel by viewModels()
    private val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPodcastDetailsBinding.inflate(inflater, container, false)
        podcastDetailsViewModel.getPodcastById(navArgs.trackId)
        podcastDetailsViewModel.getEpisodeSById(navArgs.trackId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        podcastDetailsAdapter = PodcastDetailsAdapter(listOf(), this@PodcastDetailsFragment)

        initViewModel(
            podcastDetailsViewModel.podcastUiState,
            podcastDetailsViewModel.episodesUiState,
            { setPodcastDetails() },
            { setEpisodesList() }
        )

        handelEvents()
    }

    private fun initViewModel(
        stateFlow1: StateFlow<Resource<Any>>,
        stateFlow2: StateFlow<Resource<Any>>,
        onSuccess1: () -> Unit,
        onSuccess2: () -> Unit,
    ) {
        lifecycleScope.launch {
            stateFlow1.combine(stateFlow2) { podcast, episodes ->
                Pair(podcast, episodes)
            }.collect { (podcast, episodes) ->
                when (podcast) {
                    is Resource.Loading -> {
                        progressDialog.show()
                    }

                    is Resource.Success -> {
                        delay(300)
                        progressDialog.dismiss()
                        onSuccess1()
                    }

                    is Resource.Error -> {
                        delay(300)
                        progressDialog.dismiss()
                        val msg = podcast.exception?.message ?: getString(R.string.generic_err_msg)
                        view?.showSnakeBarError(msg)
                        logDetailsIssuesToCrashlytics(msg)
                    }
                }

                // Handle the second state flow
                when (episodes) {
                    is Resource.Loading -> {
                        progressDialog.show()
                    }

                    is Resource.Success -> {
                        delay(300)
                        progressDialog.dismiss()
                        onSuccess2()
                    }

                    is Resource.Error -> {
                        delay(300)
                        progressDialog.dismiss()
                        val msg = episodes.exception?.message ?: getString(R.string.generic_err_msg)
                        view?.showSnakeBarError(msg)
                        logDetailsIssuesToCrashlytics(msg)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun handelEvents() {
        binding.imgPodcastDetailsBackArrow.setOnClickListener {
            it.findNavController().popBackStack()
        }
        binding.imgPodcastDetailsSubscription.setOnClickListener {
            lifecycleScope.launch {
                podcastDetailsViewModel.podcastUiState.collect { resource ->
                    resource.data?.let { podcastDetailsViewModel.onSubscribe(it) }
                }
            }
            Toast.makeText(
                requireContext(),
                getString(R.string.subscription_completed),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun saveToHistory() {
        lifecycleScope.launch {
//            historyEntity?.let { podcast -> podcastDetailsViewModel.saveToHistory(podcast) }
        }
    }

    private fun setPodcastDetails() = lifecycleScope.launch(Main) {
        podcastDetailsViewModel.podcastUiState.collect { resource ->
            if (resource.data?.trackId != 0L && resource.data?.trackId == navArgs.trackId) {
//                historyEntity = getHistoryEntity(podcastEntity!!)
//                saveToHistory()

                resource.data.artistName.also { binding.textPodcastDetailsArtistName.text = it }
                resource.data.collectionName.also {
                    binding.textPodcastDetailsCollectionName.text = it
                }
                resource.data.collectionName.also { binding.textAppbarTitle.text = it }
                resource.data.primaryGenreName.also {
                    binding.textPodcastDetailsGenreName.text = it
                }
                resource.data.releaseDate.changeDateFormat()
                    .also { binding.textPodcastDetailsDate.text = it }
                podcastDetailsViewModel.desc.observe(viewLifecycleOwner) { desc ->
                    desc.also { binding.textPodcastDetailsDesc.text = it }
                }
                binding.textPodcastDetailsEpisodesCount.text =
                    binding.root.resources.getString(
                        R.string.trackCount,
                        resource.data.trackCount.toString()
                    )
                Glide.with(binding.imgPodcastDetails).load(resource.data.artworkUrl100)
                    .into(binding.imgPodcastDetails)
            }
        }
    }


    private fun setEpisodesList() {
        lifecycleScope.launch(Main) {
            podcastDetailsViewModel.episodesUiState.collect { resource ->
                if (resource.data?.isNotEmpty() == true) {
                    podcastDetailsAdapter.setData((resource.data))
                    binding.recyclerViewPodcastDetails.adapter = podcastDetailsAdapter
                }
            }
        }
        lifecycleScope.launch(Main) {
            playerViewModel.currentMediaPositionInList.collect {
                if (podcastDetailsAdapter.getData().isNotEmpty()) {
                    podcastDetailsAdapter.setPlayedEpisode(podcastDetailsAdapter.getData()[it.toInt()])
                }
            }
        }
    }

    private fun getHistoryEntity(podcastEntity: PodcastEntity): HistoryEntity {
        val savedTime = System.currentTimeMillis()
        return HistoryEntity(
            navArgs.trackId,
            podcastEntity.artistName,
            podcastEntity.collectionName,
            podcastEntity.artworkUrl100,
            podcastEntity.primaryGenreName,
            podcastEntity.releaseDate,
            podcastEntity.trackCount,
            podcastEntity.trackName,
            savedTime,
        )
    }

    override fun onClickEpisode(currentEpisode: EpisodeUiState) {
        if (playerViewModel.currentEpisode.value.data?.guid != currentEpisode.guid) {
            playerViewModel.onPlayerEvents(PlayerEvents.PlayNewEpisode(currentEpisode))
        }

        val episodeDetailsBottomSheet = EpisodeDetailsBottomSheet(currentEpisode)
        childFragmentManager.let {
            episodeDetailsBottomSheet.show(it, episodeDetailsBottomSheet.tag)
        }
    }

    override fun onClickDownload(episodeUiState: EpisodeUiState) {
        lifecycleScope.launch(Main) {
            podcastDetailsViewModel.saveEpisodeToDownload(episodeUiState)
            Toast.makeText(
                requireContext(),
                "Audio ${episodeUiState.trackName} is Downloaded",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun logDetailsIssuesToCrashlytics(msg: String) {
        CrashlyticsUtils.sendCustomLogToCrashlytics<DetailsException>(
            msg,
            CrashlyticsUtils.DETAILS_KEY to msg,
        )
    }

    companion object {
        private const val TAG = "PodcastDetailsFragment"
    }
}