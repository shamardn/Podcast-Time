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
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.data.datasource.local.datastore.DataStoreKeys
import com.shamardn.podcasttime.data.datasource.local.datastore.appDataStore
import com.shamardn.podcasttime.data.model.Resource
import com.shamardn.podcasttime.databinding.FragmentHomeBinding
import com.shamardn.podcasttime.ui.common.custom_views.ProgressDialog
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import com.shamardn.podcasttime.ui.common.viewmodel.PlayerViewModel
import com.shamardn.podcasttime.ui.showSnakeBarError
import com.shamardn.podcasttime.util.CrashlyticsUtils
import com.shamardn.podcasttime.util.HomeException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment() : Fragment(), HomeInteractionListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeAdapter: HomeAdapter
    private val viewModel: HomeViewModel by viewModels()
    private val playerViewModel: PlayerViewModel by activityViewModels()
    private val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeAdapter = HomeAdapter(listOf(), this@HomeFragment)
        refreshDataOnceDaily()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        handelEvents()

        showBottomSheet()
        binding.textHomeSearch.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            this.findNavController().navigate(action)
        }
    }

    private fun handelEvents() {
        viewModel.isOnline.observe(viewLifecycleOwner) { isOnline ->
            if (!isOnline) {
                val action = HomeFragmentDirections.actionHomeFragmentToDownloadsFragment()
                this.findNavController().navigate(action)
            }
        }
    }

    private fun initViewModel() = lifecycleScope.launch(Main){
        viewModel.homeUiState.collect { resource ->
            when(resource){
                is Resource.Loading -> {
                    progressDialog.show()
                }
                is Resource.Success -> {
                    progressDialog.dismiss()
                    showPodcasts()
                }
                is Resource.Error -> {
                    progressDialog.dismiss()
                    val msg = resource.exception?.message ?: getString(R.string.generic_err_msg)
                    view?.showSnakeBarError(msg)
                    logHomeIssuesToCrashlytics(msg)
                }
            }
        }
    }

    private fun refreshDataOnceDaily() {
        viewModel.getLocalPodcasts()
        lifecycleScope.launch(Main) {
            val lastFetchTime: Flow<Long> = requireContext().appDataStore.data.map { preferences ->
                preferences[DataStoreKeys.FETCH_TIME_KEY] ?: 0
            }

            val currentTime = System.currentTimeMillis()
            requireContext().appDataStore.edit { preferences ->
                preferences[DataStoreKeys.FETCH_TIME_KEY] = currentTime
            }
            // Check if it's been more than 24 hours since the last fetch
            if (currentTime - lastFetchTime.last() > 24 * 60 * 60 * 1000) {
                // 24 hours in milliseconds
                viewModel.getRemotePodcasts()
            }
        }
    }

    private fun showPodcasts() = lifecycleScope.launch(Main) {
            viewModel.homeUiState.collect { resource ->
                if (resource.data?.isNotEmpty() == true) {
                homeAdapter.setData(resource.data)
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

    private fun logHomeIssuesToCrashlytics(msg: String) {
        CrashlyticsUtils.sendCustomLogToCrashlytics<HomeException>(
            msg,
            CrashlyticsUtils.HOME_KEY to msg,
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}