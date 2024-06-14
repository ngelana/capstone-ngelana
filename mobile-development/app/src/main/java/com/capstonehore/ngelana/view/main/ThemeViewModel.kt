package com.capstonehore.ngelana.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstonehore.ngelana.data.preferences.ThemeManager
import kotlinx.coroutines.launch

class ThemeViewModel(private val themeManager: ThemeManager) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return themeManager.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            themeManager.saveThemeSetting(isDarkModeActive)
        }
    }

}