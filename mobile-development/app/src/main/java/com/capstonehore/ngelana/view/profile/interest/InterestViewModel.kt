package com.capstonehore.ngelana.view.profile.interest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.PreferenceItem
import com.capstonehore.ngelana.data.remote.response.preferences.UserDataPreferencesItem
import com.capstonehore.ngelana.data.repository.PreferenceRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class InterestViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    fun getAllPreferences() = preferenceRepository.getAllPreferences()

    fun createUserPreference(userDataPreferencesItem: List<UserDataPreferencesItem>) =
        preferenceRepository.createUserPreference(userDataPreferencesItem)

    fun getPreferenceById() = preferenceRepository.getPreferenceByUserId()

    fun updateUserPreference(preferenceItem: List<PreferenceItem>) =
        preferenceRepository.updateUserPreference(preferenceItem)

    fun getUserId() {
        viewModelScope.launch {
            userPreferences.getUserId().first()
        }
    }

}