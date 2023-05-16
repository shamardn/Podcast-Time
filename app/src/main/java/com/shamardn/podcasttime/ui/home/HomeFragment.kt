package com.shamardn.podcasttime.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.shamardn.podcasttime.data.remote.ApiService
import com.shamardn.podcasttime.databinding.FragmentHomeBinding
import com.shamardn.podcasttime.domain.entity.Podcast
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment() : Fragment(), HomeInteractionListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeAdapter: HomeAdapter
    private var items = mutableListOf<Podcast>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchPodcasts("podcast")

        binding.textHomeSearch.setOnClickListener {

        }
    }

    private fun fetchPodcasts(term: String) {
        lifecycleScope.launch {
            withContext(lifecycleScope.coroutineContext) {
                val service = ApiService.instance
                items.clear()
                service.getPodcasts(term).results.forEach {
                    items.add(it)
                }
                Log.i("HomeFragment", "${items}")
            }
            Toast.makeText(context, "${items.size}", Toast.LENGTH_SHORT).show()
            homeAdapter = HomeAdapter(items, this@HomeFragment)
            binding.homeRecyclerView.adapter = homeAdapter
        }
    }

    override fun onClickPodcast(trackId: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToPodcastDetailsFragment(trackId)
        Navigation.findNavController(binding.root).navigate(action)
    }
}