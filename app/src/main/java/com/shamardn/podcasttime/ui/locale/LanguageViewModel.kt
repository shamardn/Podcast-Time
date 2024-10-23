package com.shamardn.podcasttime.ui.locale

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.data.datasource.local.datastore.AppConfiguration
import com.shamardn.podcasttime.util.Language
import com.shamardn.podcasttime.util.SettingsUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val appConfiguration: AppConfiguration,
) : ViewModel() {

    private val settingsUtils = SettingsUtils

    private val _currentLanguage = MutableLiveData<Language>()
    val currentLanguage: LiveData<Language> get() = _currentLanguage

    init {
        _currentLanguage.postValue(settingsUtils.getCurrentLanguage())
    }

    fun handleLanguageChange(newLanguage: Language) {
        settingsUtils.updateAppLanguage(newLanguage)
        _currentLanguage.postValue(newLanguage)
    }

    fun setNewLanguage(newLanguage: String) {
        viewModelScope.launch {
            appConfiguration.saveCurrentLanguage(newLanguage)
        }
    }
}