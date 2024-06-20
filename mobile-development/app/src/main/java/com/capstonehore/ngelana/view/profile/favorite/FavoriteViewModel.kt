package com.capstonehore.ngelana.view.profile.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonehore.ngelana.data.local.entity.Favorite
import com.capstonehore.ngelana.data.repository.FavoriteRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository): ViewModel() {

    fun getAllFavorites() = favoriteRepository.getAllFavorites()

    fun getFavoriteByPlaceId(placeId: String) = favoriteRepository.getFavoriteByPlaceId(placeId)

    fun insertFavoritePlace(favorite: Favorite) {
        viewModelScope.launch {
            favoriteRepository.insertFavoritePlace(favorite)
        }
    }

    fun deleteFavoritePlace(favorite: Favorite) {
        viewModelScope.launch {
            favoriteRepository.deleteFavoritePlace(favorite)
        }
    }

}