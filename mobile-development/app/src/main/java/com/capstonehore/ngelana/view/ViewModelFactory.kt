package com.capstonehore.ngelana.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstonehore.ngelana.GeneralRepository
import com.capstonehore.ngelana.di.UserPreference
import com.capstonehore.ngelana.view.home.HomeViewModel
import com.capstonehore.ngelana.view.login.LoginViewModel
import com.capstonehore.ngelana.view.profile.ProfileViewModel
import com.capstonehore.ngelana.view.signup.email.EmailViewModel
import com.capstonehore.ngelana.view.signup.password.PasswordViewModel

class ViewModelFactory(
        private val repository: GeneralRepository,
        private val pref: UserPreference,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        when {
//            modelClass.isAssignableFrom(HomeViewModel::class.java)     -> {
//                return HomeViewModel(repository, pref) as T
//            }
//
//            modelClass.isAssignableFrom(LoginViewModel::class.java)    -> {
//                return LoginViewModel(repository, pref) as T
//            }
//
//            modelClass.isAssignableFrom(ProfileViewModel::class.java)  -> {
//                return ProfileViewModel(repository, pref) as T
//            }
//
//            modelClass.isAssignableFrom(EmailViewModel::class.java)    -> {
//                return EmailViewModel(repository, pref) as T
//            }
//
//            modelClass.isAssignableFrom(PasswordViewModel::class.java) -> {
//                return PasswordViewModel(repository, pref) as T
//            }
//
//            else {
//                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
//            }

            TODO("create sealed class for ViewModelFactory")
        }
    }