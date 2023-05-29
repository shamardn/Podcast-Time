package com.shamardn.podcasttime.ui.podcastdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shamardn.podcasttime.databinding.EpisodeBottomSheetLayoutBinding

class EpisodeDetailsBottomSheet: BottomSheetDialogFragment() {

    private val navArgs: EpisodeDetailsBottomSheetArgs by navArgs()
    lateinit var binding: EpisodeBottomSheetLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EpisodeBottomSheetLayoutBinding.inflate(inflater,container,false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textEpisode.text = navArgs.trackViewUrl
    }
}