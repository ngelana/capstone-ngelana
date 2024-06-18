package com.capstonehore.ngelana.data.remote.retrofit

import com.capstonehore.ngelana.data.remote.response.places.PlacesResponse
import com.capstonehore.ngelana.data.remote.response.plan.PlanResponse
import com.capstonehore.ngelana.data.remote.response.preferences.PreferencesResponse
import com.capstonehore.ngelana.data.remote.response.preferences.PreferencesResponseById
import com.capstonehore.ngelana.data.remote.response.preferences.UserDataPreferencesItem
import com.capstonehore.ngelana.data.remote.response.preferences.UserPreference
import com.capstonehore.ngelana.data.remote.response.review.ReviewItem
import com.capstonehore.ngelana.data.remote.response.review.ReviewResponse
import com.capstonehore.ngelana.data.remote.response.users.LoginResponse
import com.capstonehore.ngelana.data.remote.response.users.RegisterResponse
import com.capstonehore.ngelana.data.remote.response.users.UserResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Users
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @GET("user/:id")
    suspend fun getUserById(
        @Path("id") id: String
    ): UserResponse


    // Places
    @GET("place")
    suspend fun getAllPlaces(): PlacesResponse

    @GET("place/:id")
    suspend fun getPlaceById(
        @Path("id") id: String
    ): PlacesResponse

    @GET("place/search-place")
    suspend fun searchPlaceByName(
        @Query("query") name: String,
    ): PlacesResponse


    // Plan
    @GET("plan")
    suspend fun getPlanByUserId(
        @Path("userId") userId: String
    ): PlanResponse


    // Preferences
    @GET("preference")
    suspend fun getAllPreferences(): PreferencesResponse

    @POST("preference")
    suspend fun createPreference(
        @Body userDataPreferencesItem: UserDataPreferencesItem
    ): UserPreference

    @GET("preference/:id")
    suspend fun getPreferenceById(
        @Path("id") id: String
    ): PreferencesResponseById


    // Review
    @GET("review/:id")
    suspend fun getReviewById(
        @Path("id") id: String
    ): ReviewResponse

    @POST("review")
    suspend fun createReview(
        @Body reviewItem: ReviewItem
    ): ReviewResponse

}