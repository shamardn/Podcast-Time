package com.shamardn.podcasttime.ui.episodeplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shamardn.podcasttime.databinding.EpisodeBottomSheetLayoutBinding
import com.shamardn.podcasttime.ui.common.custom_views.ProgressDialog
import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState
import com.shamardn.podcasttime.ui.common.viewmodel.PlayerViewModel
import com.shamardn.podcasttime.util.PlayerEvents
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EpisodeDetailsBottomSheet(
    var currentEpisode: EpisodeUiState,
) : BottomSheetDialogFragment() {

    val playerViewModel: PlayerViewModel by activityViewModels()

    private var _binding: EpisodeBottomSheetLayoutBinding? = null
    private val binding get() = _binding!!

    private var isFavouriteEpisode: Boolean = false

    private val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = EpisodeBottomSheetLayoutBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.playerViewModel = playerViewModel
        return binding.root
    }

    private fun handelEvents() {
        binding.apply {
            dragHandle.setOnClickListener { dismiss() }
            btnBottomSheetPlayPause.setOnClickListener { playerViewModel?.onPlayerEvents(PlayerEvents.PausePlay) }
            btnBottomSheetNext.setOnClickListener { playerViewModel?.onPlayerEvents(PlayerEvents.Next) }
            btnBottomSheetPrev.setOnClickListener { playerViewModel?.onPlayerEvents(PlayerEvents.Previous) }
            btnBottomSheetForward.setOnClickListener { playerViewModel?.onPlayerEvents(PlayerEvents.SeekForward) }
            btnBottomSheetRewind.setOnClickListener { playerViewModel?.onPlayerEvents(PlayerEvents.SeekBack) }
            seekBarBottomSheet.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean,
                ) {
                    if (fromUser) playerViewModel?.onPlayerEvents(
                        PlayerEvents.MoveToSpecificPosition(
                            progress.toLong()
                        )
                    )
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    // Do something when the user starts touching the SeekBar
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    // Do something when the user stops touching the SeekBar
                }
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handelEvents()
    }

    companion object {
        private const val TAG = "EpisodeDetailsBottomSheet"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}