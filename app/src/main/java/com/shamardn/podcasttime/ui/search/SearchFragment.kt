package com.shamardn.podcasttime.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.shamardn.podcasttime.data.remote.ApiService
import com.shamardn.podcasttime.databinding.FragmentSearchBinding
import com.shamardn.podcasttime.domain.entity.Podcast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment(), SearchInteractionListener {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: SearchAdapter
    private var items = mutableListOf<Podcast>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgSearchBackArrow.setOnClickListener {
            view.findNavController().popBackStack()
        }

        binding.searchInput.doOnTextChanged { text, start, before, count ->

            fetchPodcasts(text.toString())
        }
    }

    private fun fetchPodcasts(term: String) {
        lifecycleScope.launch {
            withContext(lifecycleScope.coroutineContext) {
                delay(1000)
                val service = ApiService.instance
                items.clear()
                service.getPodcasts(term).results.forEach {
                    items.add(it)
                }
            }
            searchAdapter = SearchAdapter(items, this@SearchFragment)
            binding.searchRecyclerView.adapter = searchAdapter
        }
    }

    override fun onClickPodcast(trackId: Int) {
        val action = SearchFragmentDirections.actionSearchFragmentToPodcastDetailsFragment(trackId)
        Navigation.findNavController(binding.root).navigate(action)
    }
}