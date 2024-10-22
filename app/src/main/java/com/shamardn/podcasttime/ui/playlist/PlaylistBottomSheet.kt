package com.shamardn.podcasttime.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.data.datasource.local.database.entity.PlaylistEntity
import com.shamardn.podcasttime.data.model.Resource
import com.shamardn.podcasttime.databinding.BotomSheetPlaylistBinding
import com.shamardn.podcasttime.ui.common.custom_views.ProgressDialog
import com.shamardn.podcasttime.ui.showSnakeBarError
import com.shamardn.podcasttime.util.CrashlyticsUtils
import com.shamardn.podcasttime.util.PlaylistException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlaylistBottomSheet : BottomSheetDialogFragment(), PlaylistsListener {

    val viewModel: PlaylistViewModel by viewModels()
    private lateinit var playListAdapter: PlaylistAdapter

    private var _binding: BotomSheetPlaylistBinding? = null
    private val binding get() = _binding!!

    private val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BotomSheetPlaylistBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchAllPlaylists()

        initViewModel()
//        handleObservers()

        playListAdapter = PlaylistAdapter(mutableListOf(), this)
        binding.recyclerPlaylist.adapter = playListAdapter

    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.playlists.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        progressDialog.show()
                        delay(500)
                    }

                    is Resource.Success -> {
                        progressDialog.dismiss()
                        getPlaylists()
                    }

                    is Resource.Error -> {
                        progressDialog.dismiss()
                        val msg = resource.exception?.message ?: getString(R.string.generic_err_msg)
                        view?.showSnakeBarError(msg)
                        logPlaylistIssuesToCrashlytics(msg)
                    }
                }
            }
        }
    }

    private fun getPlaylists() = lifecycleScope.launch(Main) {
        viewModel.playlists.collect { resource ->
            if (resource.data?.isNotEmpty() == true) {
                playListAdapter.setData(resource.data)
                binding.recyclerPlaylist.adapter = playListAdapter
            } else {
                val msg = resource.exception?.message ?: getString(R.string.generic_err_msg)
                view?.showSnakeBarError(msg)
                logPlaylistIssuesToCrashlytics(msg)
            }
        }
    }

    override fun onPlaylistClick(playlist: PlaylistEntity) {
//        TODO("Not yet implemented")
    }

    override fun onAddNewPlaylistClick() {
//        TODO("Not yet implemented")
    }

    private fun logPlaylistIssuesToCrashlytics(msg: String) {
        CrashlyticsUtils.sendCustomLogToCrashlytics<PlaylistException>(
            msg,
            CrashlyticsUtils.PLAYLIST_KEY to msg,
        )
    }
}