package com.shamardn.podcasttime.ui.download

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
import com.shamardn.podcasttime.databinding.FragmentDownloadsBinding
import com.shamardn.podcasttime.ui.common.custom_views.ProgressDialog
import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState
import com.shamardn.podcasttime.ui.common.viewmodel.PlayerViewModel
import com.shamardn.podcasttime.ui.episodeplayer.EpisodeDetailsBottomSheet
import com.shamardn.podcasttime.ui.showSnakeBarError
import com.shamardn.podcasttime.util.CrashlyticsUtils
import com.shamardn.podcasttime.util.DownloadsException
import com.shamardn.podcasttime.util.PlayerEvents
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DownloadsFragment : Fragment(), DownloadsInteractionListener {

    private var _binding: FragmentDownloadsBinding? = null
    private val binding get() = _binding!!
    private val playerViewModel: PlayerViewModel by activityViewModels()
    private val downloadViewModel: DownloadsViewModel by viewModels()
    private lateinit var downloadAdapter: DownloadsAdapter

    private val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDownloadsBinding.inflate(inflater, container, false)

        downloadViewModel.getDownloadedEpisodes()

        initViewModel()

        return binding.root
    }

    private fun initViewModel() = lifecycleScope.launch(Main) {
        downloadViewModel.downloadedEpisodesUiState.collect { resource ->
            when (resource) {
                is Resource.Loading -> {
                    progressDialog.show()
                    delay(500)
                }

                is Resource.Success -> {
                    progressDialog.dismiss()
                    showDownloadedEpisodes()
                }

                is Resource.Error -> {
                    progressDialog.dismiss()
                    val msg = resource.exception?.message ?: getString(R.string.generic_err_msg)
                    view?.showSnakeBarError(msg)
                    logDownloadsIssuesToCrashlytics(msg)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        downloadAdapter = DownloadsAdapter(listOf(), this@DownloadsFragment)

        showBottomSheet()
        handleEvents()
    }

    private fun handleEvents() {
        binding.apply {
            imgDownloadsBackArrow.setOnClickListener {
                it.findNavController().popBackStack()
            }

            imgDownloadDelete.setOnClickListener {
                if (it.isActivated) showDeleteDownloadedEpisodesListDialog()
            }
        }
    }

    private fun showDeleteDownloadedEpisodesListDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete))
            .setMessage(resources.getString(R.string.delete_confirmation_txt))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton(resources.getString(R.string.delete)) { dialog, which ->
                deleteDownloadedEpisodesList()
            }
            .show()
    }

    private fun deleteDownloadedEpisodesList() {
        downloadViewModel.deleteDownloadedEpisodesList()
        progressDialog.show()
        downloadViewModel.getDownloadedEpisodes()
        showDownloadedEpisodes()
    }

    private fun showDownloadedEpisodes() {
        lifecycleScope.launch(Main) {
            downloadViewModel.downloadedEpisodesUiState.collect { resource ->
                if (resource.data.isNullOrEmpty()) {
                    delay(500)
                    binding.lottieNoDownloads.visibility = View.VISIBLE
                    binding.recyclerDownloads.visibility = View.GONE
                    binding.imgDownloadDelete.isActivated = false
                    progressDialog.dismiss()
                } else {
                    progressDialog.dismiss()
                    binding.recyclerDownloads.visibility = View.VISIBLE
                    binding.lottieNoDownloads.visibility = View.GONE
                    downloadAdapter.setData((resource.data))
                    binding.recyclerDownloads.adapter = downloadAdapter
                    binding.imgDownloadDelete.isActivated = true
                }
            }
        }
        lifecycleScope.launch(Main) {
            playerViewModel.currentMediaPositionInList.collect {
                if (downloadAdapter.getData().isNotEmpty()) {
                    downloadAdapter.setPlayedEpisode(downloadAdapter.getData()[it.toInt()])
                }
            }
        }
    }

    override fun onEpisodeClick(currentEpisode: EpisodeUiState, position: Int) {
        if (playerViewModel.currentEpisode.value.data?.guid != currentEpisode.guid) {
            if (playerViewModel.currentEpisode.value.data?.guid != currentEpisode.guid) {
                playerViewModel.onPlayerEvents(PlayerEvents.PlayNewEpisode(currentEpisode))
            }

            val episodeDetailsBottomSheet = EpisodeDetailsBottomSheet(currentEpisode)
            childFragmentManager.let {
                episodeDetailsBottomSheet.show(it, episodeDetailsBottomSheet.tag)
            }
        }
    }

    override fun onDeleteEpisodeClick(currentEpisode: EpisodeUiState) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete))
            .setMessage(resources.getString(R.string.delete_podcast_confirmation_txt))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton(resources.getString(R.string.delete)) { dialog, which ->
                downloadViewModel.deleteEpisode(currentEpisode)
                downloadViewModel.getDownloadedEpisodes()
                showDownloadedEpisodes()
            }
            .show()
    }

    private fun showBottomSheet() {
        playerViewModel.isBottomSheetOpened.observe(viewLifecycleOwner) {
            if (it) {
                val action =
                    DownloadsFragmentDirections.actionDownloadsFragmentToEpisodeDetailsBottomSheet()
                this.findNavController().navigate(action)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun logDownloadsIssuesToCrashlytics(msg: String) {
        CrashlyticsUtils.sendCustomLogToCrashlytics<DownloadsException>(
            msg,
            CrashlyticsUtils.DOWNLOADS_KEY to msg,
        )
    }
}