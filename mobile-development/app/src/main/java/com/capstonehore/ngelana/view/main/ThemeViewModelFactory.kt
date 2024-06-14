package com.capstonehore.ngelana.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstonehore.ngelana.data.preferences.ThemeManager

class ThemeViewModelFactory(private val themeManager: ThemeManager) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(ThemeViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return ThemeViewModel(themeManager) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

}