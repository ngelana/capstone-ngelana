package com.capstonehore.ngelana.utils

import android.content.Context
import android.content.SharedPreferences

object LanguagePreference {
    private const val PREFS_NAME = "language_prefs"
    private const val KEY_LANGUAGE = "key_language"

    fun setLanguage(context: Context, languageCode: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_LANGUAGE, languageCode)
        editor.apply()
    }

    fun getLanguage(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_LANGUAGE, null)
    }

    fun clearLanguage(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(KEY_LANGUAGE)
        editor.apply()
    }
}