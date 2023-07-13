package com.shamardn.podcasttime.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shamardn.podcasttime.databinding.FragmentSettingsBinding
import com.shamardn.podcasttime.ui.main.MainActivity

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private var bottomNavigationViewVisibility = View.VISIBLE

    private fun setBottomNavigationVisibility() {
        if (activity is MainActivity) {
            (activity as MainActivity).setBottomNavigationVisibility(bottomNavigationViewVisibility)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater,container, false)
        setBottomNavigationVisibility()
        return binding.root
    }
}