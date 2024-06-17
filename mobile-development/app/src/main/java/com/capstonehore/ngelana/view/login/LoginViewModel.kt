package com.capstonehore.ngelana.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.repository.GeneralRepository
import kotlinx.coroutines.launch

class LoginViewModel(
        private val repository: GeneralRepository,
        private val preferences: UserPreferences,
): ViewModel()
{
    // Login function to call repository login function to login activity
    fun loginViewModel(email: String, password: String) = repository.login(
            email,
            password) // not yet to be implemented

    // Save login token to preferences
    fun saveLogin(token: String) {
        viewModelScope.launch {
            preferences.saveToken(token)
            preferences.login()
        }
    }
}