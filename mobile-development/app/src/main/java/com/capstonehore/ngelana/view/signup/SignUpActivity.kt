package com.capstonehore.ngelana.view.signup

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.preferences.ThemeManager
import com.capstonehore.ngelana.databinding.ActivitySignUpBinding
import com.capstonehore.ngelana.utils.LanguagePreference
import com.capstonehore.ngelana.view.main.ThemeViewModel
import com.capstonehore.ngelana.view.main.ThemeViewModelFactory
import com.capstonehore.ngelana.view.signup.name.NameFragment
import java.util.Locale

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private lateinit var themeManager: ThemeManager
    private lateinit var themeViewModel: ThemeViewModel

    private val Context.dataStore by preferencesDataStore(THEME_SETTINGS)

    private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(SESSION)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale(this)

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
            // this session is used to store user session like token
        const val SESSION = "session"

        fun setLocale(context: Context) {
            val languageCode = LanguagePreference.getLanguage(context)
            if (languageCode != null) {
                val locale = Locale(languageCode)
                Locale.setDefault(locale)

                val config = Configuration()
                config.setLocale(locale)
                @Suppress("DEPRECATION")
                context.resources.updateConfiguration(config, context.resources.displayMetrics)
            }
        }
    }
}