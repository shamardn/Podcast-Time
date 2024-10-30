package com.shamardn.podcasttime.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shamardn.podcasttime.data.datasource.local.datastore.DataStoreKeys
import com.shamardn.podcasttime.data.datasource.local.datastore.appDataStore
import com.shamardn.podcasttime.databinding.FragmentHomeBinding
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import com.shamardn.podcasttime.ui.common.viewmodel.PlayerViewModel
import com.shamardn.podcasttime.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment() : Fragment(), HomeInteractionListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeAdapter: HomeAdapter
    private val viewModel: HomeViewModel by viewModels()
    private val playerViewModel: PlayerViewModel by activityViewModels()
    private var bottomNavigationViewVisibility = View.VISIBLE

    private fun setBottomNavigationVisibility() {
        if (activity is MainActivity) {
            (activity as MainActivity).setBottomNavigationVisibility(bottomNavigationViewVisibility)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        refreshDataOnceDaily()

        viewModel.isOnline.observe(viewLifecycleOwner) { isOnline ->
            if (!isOnline) {
                val action = HomeFragmentDirections.actionHomeFragmentToDownloadsFragment()
                this.findNavController().navigate(action)
            }
        }

        setBottomNavigationVisibility()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showPodcasts()

        showBottomSheet()
        binding.textHomeSearch.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            this.findNavController().navigate(action)
        }
    }

    private fun refreshDataOnceDaily() {
        viewModel.getLocalPodcasts()
        lifecycleScope.launch {
            val lastFetchTime: Flow<Long> = requireContext().appDataStore.data.map { preferences ->
                preferences[DataStoreKeys.FETCH_TIME_KEY] ?: 0
            }

            val currentTime = System.currentTimeMillis()
            if (currentTime - lastFetchTime.last() > 24 * 60 * 60 * 1000) {
                // 24 hours in milliseconds
                viewModel.getRemotePodcasts()
                requireContext().appDataStore.edit { preferences ->
                    preferences[DataStoreKeys.FETCH_TIME_KEY] = currentTime
                }
            }
        }
    }

    private fun showPodcasts() {
        lifecycleScope.launch {
            viewModel.uiState.collect{
                val podcasts =it.podcastUiState
                homeAdapter = HomeAdapter(podcasts, this@HomeFragment)
                binding.homeRecyclerView.adapter = homeAdapter
            }
        }
    }
    override fun onClickPodcast(podcast: PodcastUiState) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToPodcastDetailsFragment(podcast.trackId)
        this.findNavController().navigate(action)
    }

    private fun showBottomSheet() {
        playerViewModel.isBottomSheetOpened.observe(viewLifecycleOwner) {
            if (it) {
                val action = HomeFragmentDirections.actionHomeFragmentToEpisodeDetailsBottomSheet()
                this.findNavController().navigate(action)
            }
        }
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}