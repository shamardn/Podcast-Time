package com.shamardn.podcasttime.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shamardn.podcasttime.databinding.FragmentHomeBinding
import com.shamardn.podcasttime.services.PodcastService
import com.shamardn.podcasttime.ui.main.MainActivity
import com.shamardn.podcasttime.util.Constants.ACTION_START_OR_RESUME_SERVICE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment() : Fragment(), HomeInteractionListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeAdapter: HomeAdapter
    private val viewModel: HomeViewModel by viewModels()
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
        viewModel.getPodcasts("podcast")

        viewModel.isOnline.observe(viewLifecycleOwner) { isOnline ->
            if(!isOnline) {
                val action = HomeFragmentDirections.actionHomeFragmentToDownloadsFragment()
                this.findNavController().navigate(action)
            }
        }

        setBottomNavigationVisibility()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchPodcasts()

        //TODO make the service start on clicking some button like play button
        sendCommandToService(ACTION_START_OR_RESUME_SERVICE)

        binding.textHomeSearch.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            this.findNavController().navigate(action)
        }
    }

    private fun fetchPodcasts() {
        lifecycleScope.launch {
            viewModel.podcasts.collect{
                if (it != null) {
                    homeAdapter = HomeAdapter(it.results, this@HomeFragment)
                    binding.homeRecyclerView.adapter = homeAdapter
                }
            }
        }
    }

    override fun onClickPodcast(trackId: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToPodcastDetailsFragment(trackId)
        this.findNavController().navigate(action)
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(), PodcastService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }
}