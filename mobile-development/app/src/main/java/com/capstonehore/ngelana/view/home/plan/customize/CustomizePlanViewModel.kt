package com.capstonehore.ngelana.view.home.plan.customize

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CustomizePlanViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("plan_prefs", Context.MODE_PRIVATE)

    private val _placeItem = MutableLiveData<MutableList<PlaceItem>>(mutableListOf())
    val placeItem: LiveData<MutableList<PlaceItem>> get() = _placeItem

    init {
        loadPlanList()
    }

    fun savePlanList(list: MutableList<PlaceItem>) {
        _placeItem.value = list
        val json = Gson().toJson(list)
        sharedPreferences.edit().putString("plan_list", json).apply()
    }

    fun loadPlanList(): MutableList<PlaceItem> {
        val json = sharedPreferences.getString("plan_list", null)
        val type = object : TypeToken<MutableList<PlaceItem>>() {}.type
        return if (json != null) {
            Gson().fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

}