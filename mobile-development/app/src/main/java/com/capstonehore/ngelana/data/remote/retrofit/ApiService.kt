package com.capstonehore.ngelana.data.remote.retrofit

import com.capstonehore.ngelana.data.Place
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("places")
    suspend fun getPlaces(): List<Place>

    @POST("login")
    suspend fun login()

    @POST("register")
    suspend fun register()

}