package com.capstonehore.ngelana.view.explore.place

import android.content.Context
import android.location.Address
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.repository.PlaceRepository
import kotlinx.coroutines.launch

class PlaceViewModel(
    private val placeRepository: PlaceRepository
//    private val repository: Repository
) : ViewModel() {

    fun getAllPlaces() = placeRepository.getAllPlaces()

    fun getPlaceById(id: String) = placeRepository.getPlaceById(id)

    fun searchPlaceByQuery(query: String) = placeRepository.searchPlaceByQuery(query)

    fun getPrimaryTypePlace(type: String) = placeRepository.getPrimaryTypePlace(type)

    private val _locationResult = MutableLiveData<Result<Address>>()
    val locationResult: LiveData<Result<Address>>
        get() = _locationResult

    fun getLocationDetails(context: Context, location: Location) {
        _locationResult.value = Result.Loading
        viewModelScope.launch {
            try {
                val result = placeRepository.getLocationDetails(context, location)
                result.observeForever {
                    _locationResult.value = it
                }
            } catch (e: Exception) {
                Log.e(TAG, "fetchLocationDetails: ${e.message}", e)
                _locationResult.value = Result.Error(e.message ?: "Unknown error")
            }
        }
    }

    companion object {
        private const val TAG = "PlaceViewModel"
    }

}