package com.capstonehore.ngelana.view.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.repository.GeneralRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: GeneralRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            userPreferences.logout()
        }
    }

}