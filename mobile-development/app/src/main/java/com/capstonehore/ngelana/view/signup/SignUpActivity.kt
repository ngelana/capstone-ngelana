package com.capstonehore.ngelana.view.signup

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.preferences.ThemeManager
import com.capstonehore.ngelana.databinding.ActivitySignUpBinding
import com.capstonehore.ngelana.view.main.ThemeViewModel
import com.capstonehore.ngelana.view.main.ThemeViewModelFactory
import com.capstonehore.ngelana.view.signup.name.NameFragment

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private lateinit var themeManager: ThemeManager
    private lateinit var themeViewModel: ThemeViewModel

    private val Context.dataStore by preferencesDataStore(THEME_SETTINGS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        themeSettings()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NameFragment())
                .commit()
        }
    }

    private fun themeSettings() {
        themeManager = ThemeManager.getInstance(dataStore)

        themeViewModel = ViewModelProvider(
            this@SignUpActivity,
            ThemeViewModelFactory(themeManager)
        )[ThemeViewModel::class.java]

        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    companion object {
        const val THEME_SETTINGS = "theme_settings"
    }
}