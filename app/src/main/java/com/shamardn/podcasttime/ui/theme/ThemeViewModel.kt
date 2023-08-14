package com.shamardn.podcasttime.ui.theme

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shamardn.podcasttime.util.SettingsUtils
import com.shamardn.podcasttime.util.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    @ApplicationContext context: Context,
): ViewModel() {

    private val settingsUtils = SettingsUtils

    private val _currentTheme = MutableLiveData<Theme>()
    val currentLTheme: LiveData<Theme> get() = _currentTheme

    init {
        _currentTheme.postValue(settingsUtils.getCurrentAppTheme(context))
    }

    fun handleThemeChange(newTheme: Theme) {
        settingsUtils.updateAppTheme(newTheme)
        _currentTheme.postValue(newTheme)
//        viewModelScope.launch {
//            dataStorePreferences.writeString(Constants.DARK_MODE, newTheme.name)
//        }
    }
}