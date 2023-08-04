package com.shamardn.podcasttime.ui

import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomPlayerFragment : Fragment() {

//    private lateinit var binding: FragmentBottomPlayerBinding
//
//    private val mediaViewModel: MediaViewModel by activityViewModels()
//
//    private var isUpdateSeeBar = true
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View {
//        binding = FragmentBottomPlayerBinding.inflate(inflater, container, false)
//
//        Log.i("BottomPlayer", "onCreateView")
//
//        binding.mainProgress.visibility = View.VISIBLE
//
//        mediaViewModel.audioList.observe(viewLifecycleOwner) {
//            Log.i("BottomPlayerFragment", it.toString())
//        }
//        setViewContent()
//        controllerMediaPlayer()
//        setUpSeekBar()
//
//        return binding.root
//    }
//
//    private fun setUpSeekBar() {
//        binding.mainSeekBar.apply {
//
//            if (isUpdateSeeBar) {
//                max = mediaViewModel.currentDuration.toInt()
//            }
//
//            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//                override fun onProgressChanged(
//                    seekBar: SeekBar?,
//                    progress: Int,
//                    fromUser: Boolean,
//                ) {
//                    /**
//                     * Y can set text time of song
//                     */
//                }
//
//                override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                    isUpdateSeeBar = false
//                }
//
//                override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                    seekBar?.let {
//                        mediaViewModel.seekTo(it.progress.toFloat())
//                        isUpdateSeeBar = true
//                    }
//                }
//            })
//        }
//    }
//
//    private fun controllerMediaPlayer() =
//        mediaViewModel.apply {
////            imgSkipPrevious.setOnClickListener {
////                it.setAlphaAnimation()
////                skipToPreviousAudio()
////            }
////            imgSkipNext.setOnClickListener {
////                it.setAlphaAnimation()
////                skipToNextAudio()
////            }
//            binding.imgMainPlayerPlay.setOnClickListener {
//                Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show()
//                it.setAlphaAnimation()
////                playOrPauseAudio()
//            }
//
//        }
//
//    private fun setViewContent() {
//        mediaViewModel.apply {
//            mediaItems.observe(viewLifecycleOwner) { resources ->
////                resources.execute(
////                    onSuccess = {
////                        binding.mainProgress.visibility = View.GONE
////                    },
////                    onError = { message ->
////                        binding.mainProgress.visibility = View.GONE
////                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
////                    },
////                    onLoading = {
////                        binding.mainProgress.visibility = View.VISIBLE
////                    }
////                )
//            }
//
//            curPlayingAudio.observe(viewLifecycleOwner) { metadataItemSong ->
//                binding.textMainPlayerEpisodeTitle.text = metadataItemSong?.description?.title
//            }
//
//            playbackState.observe(viewLifecycleOwner) { state ->
//                if (state?.state == PlaybackStateCompat.STATE_PLAYING)
//                    binding.imgMainPlayerPlay.setBackgroundResource(R.drawable.ic_pause)
//                else binding.imgMainPlayerPlay.setBackgroundResource(R.drawable.ic_play)
//                state?.position?.toInt()?.let { statePos ->
//                    binding.mainSeekBar.progress = statePos
//                }
//            }
//
//        }
//    }

}