package com.capstonehore.ngelana.data.remote.retrofit

import com.capstonehore.ngelana.data.remote.response.ReviewItem
import com.capstonehore.ngelana.data.remote.response.UserInformationItem
import com.capstonehore.ngelana.data.remote.response.places.PlaceResponseById
import com.capstonehore.ngelana.data.remote.response.places.PlacesResponse
import com.capstonehore.ngelana.data.remote.response.plan.PlanResponse
import com.capstonehore.ngelana.data.remote.response.preferences.PreferencesResponse
import com.capstonehore.ngelana.data.remote.response.preferences.PreferencesResponseByUserId
import com.capstonehore.ngelana.data.remote.response.preferences.UserDataPreferencesItem
import com.capstonehore.ngelana.data.remote.response.preferences.UserPreferenceResponse
import com.capstonehore.ngelana.data.remote.response.review.ReviewResponse
import com.capstonehore.ngelana.data.remote.response.users.LoginResponse
import com.capstonehore.ngelana.data.remote.response.users.RegisterResponse
import com.capstonehore.ngelana.data.remote.response.users.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
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

    @GET("user/{id}")
    suspend fun getUserById(
        @Path("id") id: String
    ): UserResponse

    @PATCH("user/{id}")
    suspend fun updateUserById(
        @Path("id") id: String,
        @Body userInformationItem: UserInformationItem
    ): UserResponse

    @DELETE("user/{id}")
    suspend fun deleteUserById(
        @Path("id") id: String
    ): UserResponse


    // Places

//    @GET("place")
//    suspend fun getAllPlaces(): PlacesResponse

    @GET("place/{id}")
    suspend fun getPlaceById(
        @Path("id") id: String
    ): PlaceResponseById

    @POST("place/search-place")
    suspend fun searchPlaceByQuery(
        @Query("query") query: String,
    ): PlacesResponse

    @POST("place/primary-type/{type}")
    suspend fun getPrimaryTypePlace(
        @Path("type") type: String,
    ): PlacesResponse


    // Plan
    @GET("plan")
    suspend fun getPlanByUserId(
        @Field("userId") userId: String
    ): PlanResponse


    // Preferences
    @GET("preference")
    suspend fun getAllPreferences(): PreferencesResponse

    @POST("preference")
    suspend fun createPreference(
        @Body userDataPreferencesItem: UserDataPreferencesItem
    ): UserPreferenceResponse

    @GET("preference/{id}")
    suspend fun getPreferenceByUserId(
        @Path("id") id: String
    ): PreferencesResponseByUserId


    // Review
    @GET("review")
    suspend fun getAllReviewByUserId(
        @Field("userId") userId: String
    ): ReviewResponse

    @POST("review")
    suspend fun createReview(
        @Body reviewItem: ReviewItem
    ): ReviewResponse

}