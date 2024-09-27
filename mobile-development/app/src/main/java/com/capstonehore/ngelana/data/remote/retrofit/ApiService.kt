package com.capstonehore.ngelana.data.remote.retrofit

import com.capstonehore.ngelana.data.remote.response.PlanUserItem
import com.capstonehore.ngelana.data.remote.response.PreferenceItem
import com.capstonehore.ngelana.data.remote.response.ReviewItem
import com.capstonehore.ngelana.data.remote.response.UserInformationItem
import com.capstonehore.ngelana.data.remote.response.places.PlaceResponseById
import com.capstonehore.ngelana.data.remote.response.places.PlacesResponse
import com.capstonehore.ngelana.data.remote.response.plan.PlaceRecommendedResponse
import com.capstonehore.ngelana.data.remote.response.plan.PlanRecommendModel
import com.capstonehore.ngelana.data.remote.response.plan.PlanResponse
import com.capstonehore.ngelana.data.remote.response.plan.PlanResultResponse
import com.capstonehore.ngelana.data.remote.response.preferences.PreferenceModel
import com.capstonehore.ngelana.data.remote.response.preferences.PreferencesResponse
import com.capstonehore.ngelana.data.remote.response.preferences.PreferencesResponseByUserId
import com.capstonehore.ngelana.data.remote.response.preferences.UserPreferenceResponse
import com.capstonehore.ngelana.data.remote.response.review.ReviewResponse
import com.capstonehore.ngelana.data.remote.response.users.LoginModel
import com.capstonehore.ngelana.data.remote.response.users.LoginResponse
import com.capstonehore.ngelana.data.remote.response.users.RegisterModel
import com.capstonehore.ngelana.data.remote.response.users.RegisterResponse
import com.capstonehore.ngelana.data.remote.response.users.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("user/register")
    suspend fun register(
        @Body registerModel: RegisterModel
    ): RegisterResponse

    @POST("user/login")
    suspend fun login(
        @Body loginModel: LoginModel
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
    @GET("place")
    suspend fun getAllPlaces(): PlacesResponse

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
    @POST("plan/recommend")
    suspend fun getRecommendedPlace(
        @Body planRecommendModel: PlanRecommendModel
    ): PlaceRecommendedResponse

    @POST("plan/finalize")
    suspend fun setPlanResult(
        @Body planUserItem: PlanUserItem
    ): PlanResultResponse

    @GET("plan")
    suspend fun getPlanByUserId(
        @Body userId: String
    ): PlanResponse


    // Preferences
    @GET("preference/")
    suspend fun getAllPreferences(): PreferencesResponse

    @POST("preference/")
    suspend fun createUserPreference(
        @Body request: PreferenceModel
    ): UserPreferenceResponse

    @GET("preference/{id}")
    suspend fun getPreferenceByUserId(
        @Path("id") id: String
    ): PreferencesResponseByUserId

    @PATCH("preference/{id}")
    suspend fun updateUserPreference(
        @Path("id") id: String,
        @Body preferenceItem: List<PreferenceItem>
    ): UserPreferenceResponse


    // Review
    @GET("review")
    suspend fun getAllReviewByUserId(
        @Body userId: String
    ): ReviewResponse

    @POST("review")
    suspend fun createReview(
        @Body reviewItem: ReviewItem
    ): ReviewResponse

}