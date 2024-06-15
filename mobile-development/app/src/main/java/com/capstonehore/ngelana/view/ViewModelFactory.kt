package com.capstonehore.ngelana.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstonehore.ngelana.data.repository.GeneralRepository
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.di.Injection
import com.capstonehore.ngelana.view.profile.favorite.MyFavoriteViewModel

class ViewModelFactory(
    private val repository: GeneralRepository,
    private val pref: UserPreferences,
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MyFavoriteViewModel::class.java) -> {
                return MyFavoriteViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context, pref: UserPreferences): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), pref)
            }.also { instance = it }
    }
}