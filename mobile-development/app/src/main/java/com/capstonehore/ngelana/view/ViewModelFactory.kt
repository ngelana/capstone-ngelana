package com.capstonehore.ngelana.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.repository.FavoriteRepository
import com.capstonehore.ngelana.data.repository.PlaceRepository
import com.capstonehore.ngelana.data.repository.PlanRepository
import com.capstonehore.ngelana.data.repository.PreferenceRepository
import com.capstonehore.ngelana.data.repository.Repository
import com.capstonehore.ngelana.data.repository.ReviewRepository
import com.capstonehore.ngelana.data.repository.UserRepository
import com.capstonehore.ngelana.di.Injection
import com.capstonehore.ngelana.di.dataStore
import com.capstonehore.ngelana.view.explore.place.PlaceViewModel
import com.capstonehore.ngelana.view.home.plan.PlanViewModel
import com.capstonehore.ngelana.view.login.LoginViewModel
import com.capstonehore.ngelana.view.profile.ProfileViewModel
import com.capstonehore.ngelana.view.profile.favorite.FavoriteViewModel
import com.capstonehore.ngelana.view.profile.interest.InterestViewModel
import com.capstonehore.ngelana.view.profile.review.ReviewViewModel
import com.capstonehore.ngelana.view.signup.SignUpViewModel

class ViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val placeRepository: PlaceRepository,
    private val planRepository: PlanRepository,
    private val preferenceRepository: PreferenceRepository,
    private val reviewRepository: ReviewRepository,
    private val favoriteRepository: FavoriteRepository,
//    private val repository: Repository,
    private val userPreferences: UserPreferences
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository, userPreferences) as T
            }
            modelClass.isAssignableFrom(PlaceViewModel::class.java) -> {
                PlaceViewModel(placeRepository) as T
            }
            modelClass.isAssignableFrom(PlanViewModel::class.java) -> {
                PlanViewModel(planRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository, userPreferences) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(favoriteRepository) as T
            }
            modelClass.isAssignableFrom(InterestViewModel::class.java) -> {
                InterestViewModel(preferenceRepository, userPreferences) as T
            }
            modelClass.isAssignableFrom(ReviewViewModel::class.java) -> {
                ReviewViewModel(reviewRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideUserRepository(context),
                    Injection.providePlaceRepository(context),
                    Injection.providePlanRepository(context),
                    Injection.providePreferenceRepository(context),
                    Injection.provideReviewRepository(context),
                    Injection.provideFavoriteRepository(context),
//                    Injection.provideRepository(context),
                    UserPreferences.getInstance(context.dataStore)
                ).also { instance = it }
            }
        }
    }
}