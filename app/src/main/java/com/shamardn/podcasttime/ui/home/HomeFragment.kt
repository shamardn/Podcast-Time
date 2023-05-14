package com.shamardn.podcasttime.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.data.remote.ApiService
import com.shamardn.podcasttime.databinding.FragmentHomeBinding
import com.shamardn.podcasttime.domain.entity.Podcast
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeFragment() : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    lateinit var homeAdapter: HomeAdapter
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
        binding.textHomeSearch.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun fetchPodcasts(term: String) {
        lifecycleScope.launch {
            lifecycleScope.async {
                val service = ApiService.instance
                items.clear()
                service.getPodcasts(term).results.forEach {
                    items.add(it)
                }
            }.await()
            Toast.makeText(context, "${items.size}", Toast.LENGTH_SHORT).show()
            homeAdapter = HomeAdapter(items)
            binding.homeRecyclerView.adapter = homeAdapter
        }
    }
}