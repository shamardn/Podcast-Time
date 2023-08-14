package com.shamardn.podcasttime.ui.locale

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shamardn.podcasttime.databinding.FragmentLanguageBottomSheetBinding
import com.shamardn.podcasttime.ui.main.MainActivity
import com.shamardn.podcasttime.util.Language
import com.shamardn.podcasttime.util.SettingsUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class LanguageBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentLanguageBottomSheetBinding
    private val settingsUtils = SettingsUtils
    private val viewModel: LanguageViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLanguageBottomSheetBinding.inflate(inflater, container, false)

        val currentLanguage = settingsUtils.getCurrentLanguage()
        binding.run {
            arabicLanguageRadioButton.run {
                isChecked = currentLanguage == Language.ARABIC
                setOnClickListener {
                    if (currentLanguage != Language.ARABIC) {
                        handleLanguageChange(Language.ARABIC)
                        viewModel.currentLanguage.observe(viewLifecycleOwner) {
                            saveNewLanguage(it.name)
                        }
                        dismiss()
                    }
                }
            }
            englishLanguageRadioButton.run {
                isChecked = currentLanguage == Language.ENGLISH
                setOnClickListener {
                    if (currentLanguage != Language.ENGLISH) {
                        handleLanguageChange(Language.ENGLISH)
                        viewModel.currentLanguage.observe(viewLifecycleOwner) {
                            saveNewLanguage(it.name)
                        }
                        dismiss()
                    }
                }
            }
        }

        return binding.root
    }

    private fun handleLanguageChange(newLanguage: Language) {
        viewModel.handleLanguageChange(newLanguage)
        val language = Locale(newLanguage.languageCode)
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLayoutDirection(language)
            (if (newLanguage.languageCode == "en") {
                View.LAYOUT_DIRECTION_RTL
            } else {
                View.LAYOUT_DIRECTION_LTR
            }).also { requireActivity().window.decorView.layoutDirection = it }
            val refresh = Intent(requireContext(), MainActivity::class.java)
            startActivity(refresh)
            activity?.finish()
        }
    }

    private fun saveNewLanguage(newLanguage: String) {
        viewModel.setNewLanguage(newLanguage)
    }
}