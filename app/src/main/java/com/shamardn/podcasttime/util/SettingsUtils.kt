package com.shamardn.podcasttime.util

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object SettingsUtils {
    fun updateAppLanguage(newLanguage: Language) {
        val localeList = LocaleListCompat.create(Locale(newLanguage.languageCode))
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    fun getCurrentLanguage(): Language {
        val languageCode = AppCompatDelegate.getApplicationLocales()[Constants.FIRST_INDEX]?.language ?: Locale.getDefault().language
        return Language.fromLanguageCode(languageCode)
    }

    fun updateAppTheme(newTheme: Theme) {
        when (newTheme) {
            Theme.LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            Theme.DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    fun getCurrentAppTheme(context: Context): Theme {
        return when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_NO -> Theme.LIGHT
            AppCompatDelegate.MODE_NIGHT_YES -> Theme.DARK
            else -> getCurrentAppThemeBelowAPI13(context)
        }
    }

    private fun getCurrentAppThemeBelowAPI13(context: Context): Theme {
        return when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> Theme.LIGHT
            Configuration.UI_MODE_NIGHT_YES -> Theme.DARK
            else -> Theme.LIGHT
        }
    }
}