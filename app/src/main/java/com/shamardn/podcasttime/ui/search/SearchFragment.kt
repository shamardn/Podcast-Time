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
import com.shamardn.podcasttime.databinding.FragmentSearchBinding
import com.shamardn.podcasttime.media.exoplayer.MediaViewModel
import com.shamardn.podcasttime.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchInteractionListener {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: SearchAdapter
    private val viewModel: SearchViewModel by viewModels()
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
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.searchInput.requestFocus()

        binding.searchInput.doOnTextChanged { text, start, before, count ->
            viewModel.getPodcasts(text.toString())
            binding.progressSearch.visibility = View.VISIBLE
            fetchPodcasts()
        }

        setBottomNavigationVisibility()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBottomSheet()

        binding.imgSearchBackArrow.setOnClickListener {
            view.findNavController().popBackStack()
        }
    }

    private fun fetchPodcasts() {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                viewModel.searchUiState.collect {
                    if(it.podcastUiState.isEmpty()) {
                        binding.lottieSearch.visibility = View.VISIBLE
                        binding.searchRecyclerView.visibility = View.GONE
                        binding.progressSearch.visibility = View.GONE

                    } else {
                        binding.lottieNoMobileInternet.visibility = View.GONE
                        binding.progressSearch.visibility = View.GONE
                        binding.searchRecyclerView.visibility = View.VISIBLE
                        binding.lottieSearch.visibility = View.GONE
                        searchAdapter = SearchAdapter(it.podcastUiState, this@SearchFragment)
                        binding.searchRecyclerView.adapter = searchAdapter
                    }

                }
            }
        }
    }

    override fun onClickPodcast(trackId: Long) {
        val action = SearchFragmentDirections.actionSearchFragmentToPodcastDetailsFragment(trackId)
        Navigation.findNavController(binding.root).navigate(action)
    }

    private fun showBottomSheet() {
        mediaViewModel.isBottomSheetOpened.observe(viewLifecycleOwner) {
            if (it) {
                val action = SearchFragmentDirections.actionSearchFragmentToEpisodeDetailsBottomSheet()
                this.findNavController().navigate(action)
            }
        }
    }
}