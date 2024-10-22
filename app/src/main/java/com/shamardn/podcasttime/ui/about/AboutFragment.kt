package com.shamardn.podcasttime.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.shamardn.podcasttime.databinding.FragmentAboutBinding
import com.shamardn.podcasttime.ui.common.viewmodel.PlayerViewModel
import com.shamardn.podcasttime.ui.main.MainActivity

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding
    private var bottomNavigationViewVisibility = View.GONE
    private val playerViewModel: PlayerViewModel by activityViewModels()

    private fun setBottomNavigationVisibility() {
        if (activity is MainActivity) {
            (activity as MainActivity).setBottomNavigationVisibility(bottomNavigationViewVisibility)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)

        setBottomNavigationVisibility()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBottomSheet()

        binding.imgAboutBackArrow.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    private fun showBottomSheet() {
        playerViewModel.isBottomSheetOpened.observe(viewLifecycleOwner) {
            if (it) {
                val action =
                    AboutFragmentDirections.actionAboutFragmentToEpisodeDetailsBottomSheet()
                this.findNavController().navigate(action)
            }
        }
    }
}