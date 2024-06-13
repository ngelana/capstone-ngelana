package com.capstonehore.ngelana.view.home

import android.content.Context
import android.content.SharedPreferences
import com.capstonehore.ngelana.data.Place
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlanViewModel(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun savePlanList(planList: ArrayList<Place>) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(planList)
        editor.putString(PLAN_LIST_KEY, json)
        editor.apply()
    }

    fun loadPlanList(): ArrayList<Place> {
        val gson = Gson()
        val json = sharedPreferences.getString(PLAN_LIST_KEY, null)
        val type = object : TypeToken<ArrayList<Place>>() {}.type
        return gson.fromJson(json, type) ?: ArrayList()
    }

    companion object {
        private const val PREFS_NAME = "CustomizePlanPrefs"
        private const val PLAN_LIST_KEY = "planList"
    }
}