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

    private var DELAY: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        installSplashScreen()

        window.decorView.postDelayed({
            startActivity(Intent(this@SplashScreenActivity, OnboardingActivity::class.java))
            finish()
        }, DELAY)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)

        val logo = "https://cdn.discordapp.com/attachments/1224593462951673898/1245771315370135552/Untitled_design__1___1_-removebg-preview.png?ex=6659f672&is=6658a4f2&hm=bd52d66ac5559d895030f6ee527489c5672ce1da97613b773896851bdd8d0f21&"
        Glide.with(this@SplashScreenActivity)
            .load(logo)
            .into(binding.imageLogo)
    }

}