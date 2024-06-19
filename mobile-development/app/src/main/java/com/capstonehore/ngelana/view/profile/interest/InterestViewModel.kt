package com.capstonehore.ngelana.view.profile.interest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.preferences.UserDataPreferencesItem
import com.capstonehore.ngelana.data.repository.GeneralRepository
import kotlinx.coroutines.launch

class InterestViewModel(
    private val repository: GeneralRepository,
    private val preferences: UserPreferences
) : ViewModel() {

    fun getAllPreferences() = repository.getAllPreferences()

    fun createPreference(userDataPreferencesItem: UserDataPreferencesItem) =
        repository.createPreference(userDataPreferencesItem)

    fun getPreferenceById() = repository.getPreferenceByUserId()

    fun saveUserPreferenceId(userPreferenceId: String) {
        viewModelScope.launch {
            preferences.saveUserPreferenceId(userPreferenceId)
        }
    }

}