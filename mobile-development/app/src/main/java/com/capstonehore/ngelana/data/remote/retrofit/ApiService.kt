package com.capstonehore.ngelana.data.remote.retrofit

import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.data.remote.response.LoginResponse
import com.capstonehore.ngelana.data.remote.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("places")
    suspend fun getPlaces(
    ): List<Place>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("password") password: String,
            @Field("birthdate") birthdate: String,
    ): RegisterResponse


    @FormUrlEncoded
    @POST("login")
    suspend fun login(
            @Field("email") email: String,
            @Field("password") password: String,
    ): LoginResponse


}