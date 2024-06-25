package com.capstonehore.ngelana.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.repository.Repository
import com.capstonehore.ngelana.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(
        private val userRepository: UserRepository,
//    private val repository: Repository,
    private val userPreferences: UserPreferences
): ViewModel() {

    fun doLogin(
        email: String,
        password: String
    ) = userRepository.login(email, password)

    fun saveLogin(token: String) {
        viewModelScope.launch {
            userPreferences.saveToken(token)
            userPreferences.prefLogin()
        }
    }

    fun saveUserId(userId: String) {
        viewModelScope.launch {
            userPreferences.saveUserId(userId)
            userPreferences.prefLogin()
        }
    }

    suspend fun hasUserPreference(): Boolean = userPreferences.hasUserPreferences()

}