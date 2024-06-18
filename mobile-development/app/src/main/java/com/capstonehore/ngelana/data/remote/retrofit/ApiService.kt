package com.capstonehore.ngelana.data.remote.retrofit

import com.capstonehore.ngelana.data.remote.response.LoginResponse
import com.capstonehore.ngelana.data.remote.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

//    @GET("places")
//    suspend fun getPlaces(
//    ): List<Place>

}