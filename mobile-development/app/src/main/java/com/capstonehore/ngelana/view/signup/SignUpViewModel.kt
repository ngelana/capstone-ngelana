package com.capstonehore.ngelana.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstonehore.ngelana.data.repository.UserRepository

class SignUpViewModel(
    private val userRepository: UserRepository
//    private val repository: Repository
) : ViewModel() {

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    fun setName(name: String) {
        _name.value = name
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun doRegister(
            name: String,
            email: String,
            password: String,
    ) = userRepository.register(name, email, password)
}