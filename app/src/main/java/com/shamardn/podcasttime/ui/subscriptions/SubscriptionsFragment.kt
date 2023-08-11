package com.shamardn.podcasttime.ui.subscriptions

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
import com.shamardn.podcasttime.databinding.FragmentSubscriptionsBinding
import com.shamardn.podcasttime.media.exoplayer.MediaViewModel
import com.shamardn.podcasttime.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubscriptionsFragment : Fragment(), SubscriptionsInteractionListener {
    lateinit var binding: FragmentSubscriptionsBinding
    private val viewModel: SubscriptionsViewModel by viewModels()
    private val mediaViewModel: MediaViewModel by activityViewModels()
    lateinit var adapter: SubscriptionsAdapter
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
        binding = FragmentSubscriptionsBinding.inflate(inflater, container, false)

        viewModel.getSubscribedPodcasts()
        setBottomNavigationVisibility()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchSubscribedPodcasts()

        showBottomSheet()

        binding.imgSubBackArrow.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    private fun fetchSubscribedPodcasts() {
        lifecycleScope.launch {
            viewModel.podcasts.collect{ podcasts ->
                if (podcasts != null) {
                    adapter = SubscriptionsAdapter( podcasts, this@SubscriptionsFragment)
                    binding.recyclerSub.adapter = adapter
                }
            }
        }
    }

    override fun onClickPodcast(trackId: Long) {
        val action = SubscriptionsFragmentDirections.actionSubscriptionsFragmentToPodcastDetailsFragment(trackId)
        this.findNavController().navigate(action)
    }

    private fun showBottomSheet() {
        mediaViewModel.isBottomSheetOpened.observe(viewLifecycleOwner) {
            if (it) {
                val action = SubscriptionsFragmentDirections.actionSubscriptionsFragmentToEpisodeDetailsBottomSheet()
                this.findNavController().navigate(action)
            }
        }
    }
}