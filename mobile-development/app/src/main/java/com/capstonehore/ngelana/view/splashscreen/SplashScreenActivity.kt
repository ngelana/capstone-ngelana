package com.capstonehore.ngelana.view.splashscreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.databinding.ActivitySplashScreenBinding
import com.capstonehore.ngelana.view.main.MainActivity
import com.capstonehore.ngelana.view.onboarding.OnboardingActivity
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    private lateinit var userPreferences: UserPreferences

    private val Context.sessionDataStore by preferencesDataStore(USER_SESSION)

    private var delay: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        installSplashScreen()

        setStatusBarColor()
        setupImage()
        isUserLoggedIn()
    }

    private fun setStatusBarColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
    }

    private fun setupImage() {
        val image = "https://storage.googleapis.com/ngelana-bucket/ngelana-logo/Logo_ngelana1.png"
        Glide.with(this@SplashScreenActivity)
            .load(image)
            .into(binding.imageLogo)
    }

    private fun isUserLoggedIn() {
        userPreferences = UserPreferences.getInstance(sessionDataStore)

        lifecycleScope.launch {
            try {
                userPreferences.isLoggedIn().collect { isLoggedIn ->
                    if (isLoggedIn == true) {
                        moveToMain()
                    } else {
                        moveToOnboarding()
                    }
                }
            } catch (_: CancellationException) {
            } catch (e: Exception) {
                Log.e(TAG, "Error in collecting user login status", e)
            }
        }
    }

    private fun moveToOnboarding() {
        window.decorView.postDelayed({
            startActivity(Intent(this@SplashScreenActivity, OnboardingActivity::class.java))
            finish()
        }, delay)
    }

    private fun moveToMain() {
        window.decorView.postDelayed({
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            finish()
        }, delay)
    }

    companion object {
        private const val TAG = "SplashScreenActivity"
        const val USER_SESSION = "user_session"
    }
}