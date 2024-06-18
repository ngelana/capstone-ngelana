package com.capstonehore.ngelana.view.explore.place.culinary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.data.repository.GeneralRepository
import kotlinx.coroutines.launch
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.remote.response.places.PlacesResponse


//! GET
class CulinarySpotViewModel(
        private val repository: GeneralRepository
) : ViewModel() {

}