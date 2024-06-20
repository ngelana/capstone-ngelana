package com.capstonehore.ngelana.view.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.UserInformationItem
import com.capstonehore.ngelana.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    fun getUserById() = userRepository.getUserById()

    fun updateUserById(userInformationItem: UserInformationItem) =
        userRepository.updateUserById(userInformationItem)

    fun deleteUserById() = userRepository.deleteUserById()

    fun logout() {
        viewModelScope.launch {
            userPreferences.logout()
        }
    }

}