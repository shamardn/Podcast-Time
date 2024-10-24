package com.shamardn.podcasttime.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.databinding.FragmentHistoryBinding
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import com.shamardn.podcasttime.ui.common.viewmodel.PlayerViewModel
import com.shamardn.podcasttime.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HistoryFragment : Fragment(), HistoryInteractionListener {
    lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryViewModel by viewModels()
    private val playerViewModel: PlayerViewModel by activityViewModels()
    lateinit var adapter: HistoryAdapter
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
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        viewModel.getHistoryPodcasts()
        setBottomNavigationVisibility()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressHistory.visibility = View.VISIBLE
        fetchHistoryPodcasts()

        showBottomSheet()

        binding.imgHistoryBackArrow.setOnClickListener {
            it.findNavController().popBackStack()
        }
        binding.imgHistoryDelete.setOnClickListener {
            if(it.isActivated) showDeleteHistoryListDialog()
        }
    }

    private fun showDeleteHistoryListDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete))
            .setMessage(resources.getString(R.string.delete_confirmation_txt))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton(resources.getString(R.string.delete)) { dialog, which ->
                deleteHistoryList()
            }
            .show()
    }

    private fun showDeletePodcastFromHistoryDialog(podcast: PodcastUiState) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete))
            .setMessage(resources.getString(R.string.delete_podcast_confirmation_txt))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton(resources.getString(R.string.delete)) { dialog, which ->
                deletePodcastFromHistory(podcast)
            }
            .show()
    }

    private fun deleteHistoryList() {
        viewModel.deleteHistoryList()
        binding.progressHistory.visibility = View.VISIBLE
        viewModel.getHistoryPodcasts()
        fetchHistoryPodcasts()
    }

    private fun deletePodcastFromHistory(podcast: PodcastUiState) {
        viewModel.deletePodcastFromHistory(podcast)
        binding.progressHistory.visibility = View.VISIBLE
        viewModel.getHistoryPodcasts()
        fetchHistoryPodcasts()
    }

    private fun fetchHistoryPodcasts() {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                viewModel.historyUiState.collect {
                    if(it.podcastUiState.isEmpty()) {
                        binding.lottieNoHistory.visibility = View.VISIBLE
                        binding.recyclerHistory.visibility = View.GONE
                        binding.progressHistory.visibility = View.GONE
                        binding.imgHistoryDelete.isActivated = false
                    } else {
                        binding.progressHistory.visibility = View.GONE
                        binding.recyclerHistory.visibility = View.VISIBLE
                        binding.lottieNoHistory.visibility = View.GONE
                        adapter = HistoryAdapter(it.podcastUiState, this@HistoryFragment)
                        binding.recyclerHistory.adapter = adapter
                        binding.imgHistoryDelete.isActivated = true
                    }
                }
            }
        }
    }

    override fun onClickPodcast(trackId: Long) {
        val action = HistoryFragmentDirections.actionHistoryFragmentToPodcastDetailsFragment(trackId)
        this.findNavController().navigate(action)
    }

    override fun onDeletePodcastFromHistoryClick(podcast: PodcastUiState) {
        showDeletePodcastFromHistoryDialog(podcast)
    }

    private fun showBottomSheet() {
        playerViewModel.isBottomSheetOpened.observe(viewLifecycleOwner) {
            if (it) {
                val action = HistoryFragmentDirections.actionHistoryFragmentToEpisodeDetailsBottomSheet()
                this.findNavController().navigate(action)
            }
        }
    }
}