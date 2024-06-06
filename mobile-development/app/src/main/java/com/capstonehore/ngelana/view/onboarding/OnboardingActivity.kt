package com.capstonehore.ngelana.view.onboarding

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.databinding.ActivityOnboardingBinding
import com.capstonehore.ngelana.view.login.LoginActivity
import com.capstonehore.ngelana.view.register.RegisterActivity
import com.capstonehore.ngelana.view.signup.interest.InterestFragment

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupStatusBar()
    }

    private fun setupAction() {
        binding.interestButton.setOnClickListener {
            val myFragment = InterestFragment()
            val fragmentManager: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main, myFragment)
            fragmentTransaction.commit()
        }
        binding.signupButton.setOnClickListener {
            startActivity(Intent(this@OnboardingActivity, RegisterActivity::class.java))
            finish()
        }
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this@OnboardingActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun setupStatusBar() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.dark_blue)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            }
        }
    }

}