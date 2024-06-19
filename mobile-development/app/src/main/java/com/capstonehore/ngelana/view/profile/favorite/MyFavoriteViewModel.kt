package com.capstonehore.ngelana.view.profile.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonehore.ngelana.data.local.entity.Favorite
import com.capstonehore.ngelana.data.repository.GeneralRepository
import kotlinx.coroutines.launch

class MyFavoriteViewModel(private val repository: GeneralRepository): ViewModel() {

    fun getAllFavorites() = repository.getAllFavorites()

    fun getFavoriteByPlaceId(placeId: String) = repository.getFavoriteByPlaceId(placeId)

    fun insertFavoritePlace(favorite: Favorite) {
        viewModelScope.launch {
            repository.insertFavoritePlace(favorite)
        }
    }

    fun deleteFavoritePlace(favorite: Favorite) {
        viewModelScope.launch {
            repository.deleteFavoritePlace(favorite)
        }
    }

}