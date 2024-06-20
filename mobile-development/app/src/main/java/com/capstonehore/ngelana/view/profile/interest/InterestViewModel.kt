package com.capstonehore.ngelana.view.profile.interest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.PreferenceItem
import com.capstonehore.ngelana.data.remote.response.preferences.UserDataPreferencesItem
import com.capstonehore.ngelana.data.repository.GeneralRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class InterestViewModel(
    private val repository: GeneralRepository,
    private val preferences: UserPreferences
) : ViewModel() {

    fun getAllPreferences() = repository.getAllPreferences()

    fun createUserPreference(userDataPreferencesItem: List<UserDataPreferencesItem>) =
        repository.createUserPreference(userDataPreferencesItem)

    fun getPreferenceById() = repository.getPreferenceByUserId()

    fun updateUserPreference(preferenceItem: List<PreferenceItem>) =
        repository.updateUserPreference(preferenceItem)

    fun getUserId() {
        viewModelScope.launch {
            preferences.getUserId().first()
        }
    }

}