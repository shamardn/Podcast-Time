package com.shamardn.podcasttime.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.shamardn.podcasttime.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchInteractionListener {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: SearchAdapter
    private val viewModel: SearchViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.searchInput.doOnTextChanged { text, start, before, count ->
            viewModel.getPodcasts(text.toString())
            fetchPodcasts()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgSearchBackArrow.setOnClickListener {
            view.findNavController().popBackStack()
        }
    }

    private fun fetchPodcasts() {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                viewModel.podcasts.collect {
                    if (it != null) {
                        searchAdapter = SearchAdapter(it.results, this@SearchFragment)
                        binding.searchRecyclerView.adapter = searchAdapter
                    }
                }
            }
        }
    }

    override fun onClickPodcast(trackId: Int) {
        val action = SearchFragmentDirections.actionSearchFragmentToPodcastDetailsFragment(trackId)
        Navigation.findNavController(binding.root).navigate(action)
    }
}