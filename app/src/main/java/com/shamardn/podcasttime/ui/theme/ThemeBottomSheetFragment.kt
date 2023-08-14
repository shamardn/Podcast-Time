package com.shamardn.podcasttime.ui.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shamardn.podcasttime.databinding.FragmentThemeBottomSheetBinding
import com.shamardn.podcasttime.util.SettingsUtils
import com.shamardn.podcasttime.util.Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThemeBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentThemeBottomSheetBinding
    private val settingsUtils = SettingsUtils
    private val viewModel: ThemeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentThemeBottomSheetBinding.inflate(inflater, container, false)

        val currentTheme = settingsUtils.getCurrentAppTheme(requireContext())

        binding.run {
            lightThemeRadioButton.run {
                isChecked = currentTheme == Theme.LIGHT
                setOnClickListener {
                    if (currentTheme != Theme.LIGHT) {
                        handleThemeChange(Theme.LIGHT)
                        dismiss()
                    }
                }
            }
            darkThemeRadioButton.run {
                isChecked = currentTheme == Theme.DARK
                setOnClickListener {
                    if (currentTheme != Theme.DARK) {
                        handleThemeChange(Theme.DARK)
                        dismiss()
                    }
                }
            }
        }

        return binding.root
    }



    private fun handleThemeChange(newTheme: Theme) {
        viewModel.handleThemeChange(newTheme)
    }
}