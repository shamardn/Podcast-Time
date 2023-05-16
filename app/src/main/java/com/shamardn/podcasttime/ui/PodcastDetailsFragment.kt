package com.shamardn.podcasttime.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.shamardn.podcasttime.databinding.FragmentPodcastDetailsBinding

class PodcastDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPodcastDetailsBinding
    private val navArgs: PodcastDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPodcastDetailsBinding.inflate(inflater, container, false)

        Toast.makeText(context, "${navArgs.trackId}", Toast.LENGTH_SHORT).show()
        return binding.root
    }
}