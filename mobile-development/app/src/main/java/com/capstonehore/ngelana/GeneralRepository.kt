package com.capstonehore.ngelana

import com.capstonehore.ngelana.data.remote.retrofit.ApiService
import com.capstonehore.ngelana.di.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class GeneralRepository(
        private var apiService: ApiService,
        private val userPreference: UserPreference,
) {
    // set token variable
    private var token: String? = null

    // get token to be used in the repository
    private suspend fun getToken(): String? = token ?: runBlocking {
        userPreference.getToken().first()
    }.also { token = it }

    companion object {
        //set instance to GeneralRepository
        private var instance: GeneralRepository? = null
    }
}