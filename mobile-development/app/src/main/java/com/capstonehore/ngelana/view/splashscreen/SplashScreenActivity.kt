package com.capstonehore.ngelana.view.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.databinding.ActivitySplashScreenBinding
import com.capstonehore.ngelana.view.onboarding.OnboardingActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    private var delay: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        installSplashScreen()

        setStatusBarColor()
        setupImage()
        moveToOnboarding()
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

    private fun moveToOnboarding() {
        window.decorView.postDelayed({
            startActivity(Intent(this@SplashScreenActivity, OnboardingActivity::class.java))
            finish()
        }, delay)
    }

}