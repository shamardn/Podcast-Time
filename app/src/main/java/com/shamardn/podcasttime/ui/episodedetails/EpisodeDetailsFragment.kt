package com.shamardn.podcasttime.ui.episodedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shamardn.podcasttime.databinding.FragmentEpisodeDetailsBinding

class EpisodeDetailsFragment : Fragment() {
    private lateinit var binding : FragmentEpisodeDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEpisodeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
}