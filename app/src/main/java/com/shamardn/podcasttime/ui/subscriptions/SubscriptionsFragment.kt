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
import com.shamardn.podcasttime.data.model.Resource
import com.shamardn.podcasttime.databinding.FragmentSubscriptionsBinding
import com.shamardn.podcasttime.ui.common.custom_views.ProgressDialog
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import com.shamardn.podcasttime.ui.common.viewmodel.PlayerViewModel
import com.shamardn.podcasttime.ui.showSnakeBarError
import com.shamardn.podcasttime.util.CrashlyticsUtils
import com.shamardn.podcasttime.util.SubscriptionException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubscriptionsFragment : Fragment(), SubscriptionsInteractionListener {
    lateinit var binding: FragmentSubscriptionsBinding
    private val viewModel: SubscriptionsViewModel by viewModels()
    private val playerViewModel: PlayerViewModel by activityViewModels()
    lateinit var adapter: SubscriptionsAdapter
    private val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSubscriptionsBinding.inflate(inflater, container, false)

        adapter = SubscriptionsAdapter(listOf(), this)
        viewModel.getSubscribedPodcasts()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()

        showBottomSheet()

        binding.imgSubDelete.setOnClickListener {
            if (it.isActivated) showDeleteSubscriptionListDialog()
        }
        binding.imgSubBackArrow.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    private fun initViewModel() = lifecycleScope.launch(Main) {
        viewModel.subscriptionsUiState.collect { resource ->
            when( resource) {
                is Resource.Loading -> {
                    progressDialog.show()
                }
                is Resource.Success -> {
                    progressDialog.dismiss()
                    showSubscribedPodcasts()
                }
                is Resource.Error -> {
                    progressDialog.dismiss()
                    val msg = resource.exception?.message ?: getString(R.string.generic_err_msg)
                    view?.showSnakeBarError(msg)
                    logSubscriptionIssuesToCrashlytics(msg)
                }
            }
        }

    }

    private fun logSubscriptionIssuesToCrashlytics(msg: String) {
        CrashlyticsUtils.sendCustomLogToCrashlytics<SubscriptionException>(
            msg,
            CrashlyticsUtils.SUBSCRIPTIONS_KEY to msg,
        )
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
        progressDialog.dismiss()
        viewModel.getSubscribedPodcasts()
        showSubscribedPodcasts()
    }

    private fun showSubscribedPodcasts() = lifecycleScope.launch(Main) {
        viewModel.subscriptionsUiState.collect { resource ->
            if (resource.data.isNullOrEmpty()) {
                binding.lottieNoSubs.visibility = View.VISIBLE
                binding.recyclerSub.visibility = View.GONE
                binding.imgSubDelete.isActivated = false
                progressDialog.dismiss()
            } else {
                progressDialog.dismiss()
                binding.recyclerSub.visibility = View.VISIBLE
                binding.lottieNoSubs.visibility = View.GONE
                adapter.setData((resource.data))
                binding.recyclerSub.adapter = adapter
                binding.imgSubDelete.isActivated = true
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
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete))
            .setMessage(resources.getString(R.string.delete_podcast_confirmation_txt))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton(resources.getString(R.string.delete)) { dialog, which ->
                viewModel.unsubscribe(podcast)
                viewModel.getSubscribedPodcasts()
                showSubscribedPodcasts()
            }
            .show()
    }

    private fun showBottomSheet() {
        playerViewModel.isBottomSheetOpened.observe(viewLifecycleOwner) {
            if (it) {
                val action =
                    SubscriptionsFragmentDirections.actionSubscriptionsFragmentToEpisodeDetailsBottomSheet()
                this.findNavController().navigate(action)
            }
        }
    }
}