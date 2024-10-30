package com.shamardn.podcasttime.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.carousel.CarouselLayoutManager
import com.shamardn.podcasttime.databinding.FragmentSearchBinding
import com.shamardn.podcasttime.ui.common.viewmodel.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchInteractionListener {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: SearchAdapter
    private val viewModel: SearchViewModel by viewModels()
    private val playerViewModel: PlayerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        searchAdapter = SearchAdapter(emptyList(), this)
        viewModel.fetchPodcastsLocally()
        fetchPodcastsLocally()
        binding.searchInput.requestFocus()
        binding.searchInput.doOnTextChanged { text, start, before, count ->
            viewModel.searchPodcastsLocally(text.toString())
            binding.searchInput.visibility = View.VISIBLE
            searchPodcastsLocally()
        }

        binding.carouselRecyclerView.layoutManager = CarouselLayoutManager()
        (binding.carouselRecyclerView.layoutManager as CarouselLayoutManager).orientation = CarouselLayoutManager.HORIZONTAL
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBottomSheet()

        binding.imgSearchBackArrow.setOnClickListener {
            view.findNavController().popBackStack()
        }
    }

    private fun searchPodcastsLocally() = lifecycleScope.launch(Main) {
        viewModel.searchUiState.collect {
            if (it.podcastUiState.isEmpty()) {
                binding.lottieSearch.visibility = View.VISIBLE
                binding.carouselRecyclerView.visibility = View.GONE
                binding.rvSearchExploreRecent.visibility = View.GONE
                binding.tvSearchExploreRecentTitle.visibility = View.GONE
                binding.tvSearchExploreFavouriteTitle.visibility = View.GONE
                binding.rvSearchExploreFavourite.visibility = View.GONE
            } else {
                binding.lottieNoMobileInternet.visibility = View.GONE
                binding.lottieSearch.visibility = View.GONE
                binding.carouselRecyclerView.visibility = View.VISIBLE
                binding.rvSearchExploreRecent.visibility = View.GONE
                binding.tvSearchExploreRecentTitle.visibility = View.GONE
                binding.tvSearchExploreFavouriteTitle.visibility = View.GONE
                binding.rvSearchExploreFavourite.visibility = View.GONE
                binding.carouselRecyclerView.adapter = searchAdapter
                searchAdapter = SearchAdapter(it.podcastUiState, this@SearchFragment)
            }
        }
    }

    private fun fetchPodcastsLocally() = lifecycleScope.launch(Main) {
        viewModel.searchUiState.collect {
            if (it.podcastUiState.isEmpty()) {
                binding.lottieSearch.visibility = View.VISIBLE
                binding.carouselRecyclerView.visibility = View.GONE
                binding.rvSearchExploreRecent.visibility = View.GONE
                binding.tvSearchExploreRecentTitle.visibility = View.GONE
                binding.tvSearchExploreFavouriteTitle.visibility = View.GONE
                binding.rvSearchExploreFavourite.visibility = View.GONE
            } else {
                binding.lottieNoMobileInternet.visibility = View.GONE
                binding.lottieSearch.visibility = View.GONE
                binding.carouselRecyclerView.visibility = View.VISIBLE
                binding.rvSearchExploreRecent.visibility = View.GONE
                binding.tvSearchExploreRecentTitle.visibility = View.GONE
                binding.tvSearchExploreFavouriteTitle.visibility = View.GONE
                binding.rvSearchExploreFavourite.visibility = View.GONE
                searchAdapter = SearchAdapter(it.podcastUiState, this@SearchFragment)
                binding.carouselRecyclerView.adapter = searchAdapter
            }
        }
    }

override fun onClickPodcast(trackId: Long) {
    val action = SearchFragmentDirections.actionSearchFragmentToPodcastDetailsFragment(trackId)
    Navigation.findNavController(binding.root).navigate(action)
}

private fun showBottomSheet() {
    playerViewModel.isBottomSheetOpened.observe(viewLifecycleOwner) {
        if (it) {
            val action = SearchFragmentDirections.actionSearchFragmentToEpisodeDetailsBottomSheet()
            this.findNavController().navigate(action)
        }
    }
}
}