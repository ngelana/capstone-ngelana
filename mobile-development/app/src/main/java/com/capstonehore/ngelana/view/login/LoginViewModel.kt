package com.capstonehore.ngelana.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.repository.GeneralRepository
import kotlinx.coroutines.launch

class LoginViewModel(
        private val repository: GeneralRepository,
        private val preferences: UserPreferences,
): ViewModel() {

    fun doLogin(
        email: String,
        password: String
    ) = repository.login(email, password)

    fun saveLogin(token: String) {
        viewModelScope.launch {
            preferences.saveToken(token)
            preferences.prefLogin()
        }
    }

    fun saveUserId(userId: String) {
        viewModelScope.launch {
            preferences.saveUserId(userId)
            preferences.prefLogin()
        }
    }

    suspend fun hasUserPreference(): Boolean = preferences.hasUserPreferences()

}