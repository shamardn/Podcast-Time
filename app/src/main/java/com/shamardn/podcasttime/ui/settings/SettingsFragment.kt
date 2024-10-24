package com.shamardn.podcasttime.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.databinding.FragmentSettingsBinding
import com.shamardn.podcasttime.ui.common.viewmodel.PlayerViewModel
import com.shamardn.podcasttime.ui.main.MainActivity
import com.shamardn.podcasttime.util.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val playerViewModel: PlayerViewModel by activityViewModels()
    private val viewModel: SettingsViewModel by viewModels()
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

        binding.cardSettingsAbout.setOnClickListener {
            viewModel.onAboutClick()
        }
        binding.cardSettingsTheme.setOnClickListener {
            viewModel.onClickTheme()
        }
        binding.cardSettingsLanguage.setOnClickListener {
            viewModel.onClickLanguage()
        }
        return binding.root
    }
    private fun setCurrentConfig() {
        lifecycleScope.launch {
            viewModel.currentLang.observe(viewLifecycleOwner) {
                binding.textSettingsChosenLanguage.text = if(it == "ENGLISH") {
                    it
                }else {
                    getString(R.string.arabic)
                }
            }
            viewModel.currentTheme.observe(viewLifecycleOwner) {
                binding.textSettingsChosenTheme.text = if(it == "DARK") {
                    it
                }else {
                    getString(R.string.light)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showMediaBottomSheet()
        handleEvents()
        setCurrentConfig()
    }

    private fun handleEvents() {
       viewModel.run {
           languageChoiceClicked.observeEvent(viewLifecycleOwner) { clicked ->
               if (clicked) {
                   showLanguageBottomSheet()
               }
           }
           themeChoiceClicked.observeEvent(viewLifecycleOwner) { clicked ->
               if (clicked) {
                   showThemeBottomSheet()
               }
           }

           navigateToAbout.observeEvent(viewLifecycleOwner) { clicked ->
               if (clicked) {
                   navigateToAboutFragment()
               }
           }
       }
    }

    private fun showThemeBottomSheet() {
        val action =
            SettingsFragmentDirections.actionSettingsFragmentToThemeBottomSheetFragment()
        this.findNavController().navigate(action)
    }
    private fun showLanguageBottomSheet() {
        val action =
            SettingsFragmentDirections.actionSettingsFragmentToLanguageBottomSheetFragment()
        this.findNavController().navigate(action)
    }

    private fun navigateToAboutFragment() {
        val action = SettingsFragmentDirections.actionSettingsFragmentToAboutFragment()
        this.findNavController().navigate(action)
    }

    private fun showMediaBottomSheet() {
        playerViewModel.isBottomSheetOpened.observe(viewLifecycleOwner) {
            if (it) {
                val action =
                    SettingsFragmentDirections.actionSettingsFragmentToEpisodeDetailsBottomSheet()
                this.findNavController().navigate(action)
            }
        }
    }
}