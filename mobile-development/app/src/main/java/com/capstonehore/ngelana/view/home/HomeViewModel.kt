package com.capstonehore.ngelana.view.home

import android.content.Context
import android.location.Address
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.repository.GeneralRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: GeneralRepository): ViewModel() {

    private val _locationResult = MutableLiveData<Result<Address>>()
    val locationResult: LiveData<Result<Address>>
        get() = _locationResult

    fun updateLocationUI(context: Context, location: Location) {
        viewModelScope.launch {
            _locationResult.value = Result.Loading
            try {
                val result = repository.getLocationDetails(context, location)
                result.observeForever {
                    _locationResult.value = it
                }
            } catch (e: Exception) {
                Log.e(TAG, "updateLocationUI: ${e.message}", e)
                _locationResult.value = Result.Error(e.message ?: "Unknown error")
            }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }

}