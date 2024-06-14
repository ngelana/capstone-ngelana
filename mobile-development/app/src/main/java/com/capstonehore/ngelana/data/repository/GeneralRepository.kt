package com.capstonehore.ngelana.data.repository

import com.capstonehore.ngelana.data.remote.retrofit.ApiService
import com.capstonehore.ngelana.data.preferences.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class GeneralRepository(
    private var apiService: ApiService,
    private val userPreferences: UserPreferences,
) {
    // set token variable
    private var token: String? = null

    // get token to be used in the repository
    private suspend fun getToken(): String? = token ?: runBlocking {
        userPreferences.getToken().first()
    }.also { token = it }

    companion object {
        //set instance to GeneralRepository
        private var instance: GeneralRepository? = null
    }
}