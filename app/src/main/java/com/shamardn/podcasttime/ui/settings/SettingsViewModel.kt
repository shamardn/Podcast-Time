package com.shamardn.podcasttime.ui.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shamardn.podcasttime.data.datasource.local.datastore.AppConfiguration
import com.shamardn.podcasttime.util.Event
import com.shamardn.podcasttime.util.SettingsUtils
import com.shamardn.podcasttime.util.postEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val appConfiguration: AppConfiguration,

    ) : ViewModel() {

    private val settingsUtils = SettingsUtils

    private val _themeChoiceClicked = MutableLiveData(Event(false))
    val themeChoiceClicked: LiveData<Event<Boolean>> get() = _themeChoiceClicked

    private val _languageChoiceClicked = MutableLiveData(Event(false))
    val languageChoiceClicked: LiveData<Event<Boolean>> get() = _languageChoiceClicked

    private val _navigateToAbout = MutableLiveData<Event<Boolean>>()
    val navigateToAbout: LiveData<Event<Boolean>> get() = _navigateToAbout

    private val _currentLang = MutableLiveData<String>()
    val currentLang: LiveData<String> = _currentLang

    private val _currentTheme = MutableLiveData<String>()
    val currentTheme: LiveData<String> = _currentTheme

    init {
        _currentLang.postValue(settingsUtils.getCurrentLanguage().name)
        _currentTheme.postValue(settingsUtils.getCurrentAppTheme(context).name)
    }

    fun onAboutClick() {
        _navigateToAbout.postEvent(true)
    }

    fun onClickLanguage() {
        _languageChoiceClicked.postEvent(true)
    }

    fun onClickTheme() {
        _themeChoiceClicked.postEvent(true)
    }

}