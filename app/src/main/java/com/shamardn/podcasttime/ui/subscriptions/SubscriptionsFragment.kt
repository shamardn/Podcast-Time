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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.databinding.FragmentSubscriptionsBinding
import com.shamardn.podcasttime.media.exoplayer.MediaViewModel
import com.shamardn.podcasttime.ui.main.MainActivity
import com.shamardn.podcasttime.ui.subscriptions.uistate.PodcastUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        binding.imgSubDelete.setOnClickListener {
            if (it.isActivated) showDeleteSubscriptionListDialog()
        }
        binding.imgSubBackArrow.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    private fun showDeleteSubscriptionListDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete))
            .setMessage(resources.getString(R.string.delete_confirmation_txt))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton(resources.getString(R.string.delete)) { dialog, which ->
                deleteSubscriptionList()
            }
            .show()
    }

    private fun deleteSubscriptionList() {
        viewModel.deleteSubscriptionList()
        binding.progressSubs.visibility = View.VISIBLE
        viewModel.getSubscribedPodcasts()
        fetchSubscribedPodcasts()
    }

    private fun fetchSubscribedPodcasts() {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                viewModel.subscriptionsUiState.collect { podcasts ->
                    if (podcasts.podcastUiState.isEmpty()) {
                        binding.lottieNoSubs.visibility = View.VISIBLE
                        binding.recyclerSub.visibility = View.GONE
                        binding.progressSubs.visibility = View.GONE
                        binding.imgSubDelete.isActivated = false
                    } else {
                        binding.progressSubs.visibility = View.GONE
                        binding.recyclerSub.visibility = View.VISIBLE
                        binding.lottieNoSubs.visibility = View.GONE
                        adapter = SubscriptionsAdapter(
                            podcasts.podcastUiState,
                            this@SubscriptionsFragment
                        )
                        binding.recyclerSub.adapter = adapter
                        binding.imgSubDelete.isActivated = true
                    }
                }
            }
        }
    }

    override fun onClickPodcast(trackId: Long) {
        val action =
            SubscriptionsFragmentDirections.actionSubscriptionsFragmentToPodcastDetailsFragment(
                trackId
            )
        this.findNavController().navigate(action)
    }

    override fun onLongClickPodcast(podcast: PodcastUiState) {
        viewModel.unsubscribe(podcast)
        viewModel.getSubscribedPodcasts()
        fetchSubscribedPodcasts()
    }

    private fun showBottomSheet() {
        mediaViewModel.isBottomSheetOpened.observe(viewLifecycleOwner) {
            if (it) {
                val action =
                    SubscriptionsFragmentDirections.actionSubscriptionsFragmentToEpisodeDetailsBottomSheet()
                this.findNavController().navigate(action)
            }
        }
    }
}