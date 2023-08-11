package com.shamardn.podcasttime.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.shamardn.podcasttime.databinding.FragmentLibraryBinding
import com.shamardn.podcasttime.media.exoplayer.MediaViewModel
import com.shamardn.podcasttime.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LibraryFragment : Fragment() {

    lateinit var binding: FragmentLibraryBinding
    private val mediaViewModel: MediaViewModel by activityViewModels()
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
        binding = FragmentLibraryBinding.inflate(inflater, container, false)

        handleEvents()
        setBottomNavigationVisibility()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomSheet()
    }

    private fun handleEvents() {
        binding.cardLibraryDownloads.setOnClickListener {
            navigateToDownloads()
        }
        binding.cardLibrarySubscriptions.setOnClickListener {
            navigateToSubscriptions()
        }
    }

    private fun navigateToDownloads() {
        val action = LibraryFragmentDirections.actionLibraryFragmentToDownloadsFragment()
        this.findNavController().navigate(action)
    }

    private fun navigateToSubscriptions() {
        val action = LibraryFragmentDirections.actionLibraryFragmentToSubscriptionsFragment()
        this.findNavController().navigate(action)
    }

    private fun showBottomSheet() {
        mediaViewModel.isBottomSheetOpened.observe(viewLifecycleOwner) {
            if (it) {
                val action =
                    LibraryFragmentDirections.actionLibraryFragmentToEpisodeDetailsBottomSheet()
                this.findNavController().navigate(action)
            }
        }
    }
}