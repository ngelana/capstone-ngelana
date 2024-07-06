package com.capstonehore.ngelana.data.remote.response.users

data class RegisterModel(
    val name: String,
    val email: String,
    val password: String
)

data class LoginModel(
    val email: String,
    val password: String
)
