package com.capstonehore.ngelana.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.preferences.ThemeManager
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.databinding.ActivityLoginBinding
import com.capstonehore.ngelana.databinding.CustomAlertDialogBinding
import com.capstonehore.ngelana.utils.LanguagePreference
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.login.interest.InterestActivity
import com.capstonehore.ngelana.view.main.MainActivity
import com.capstonehore.ngelana.view.main.ThemeViewModel
import com.capstonehore.ngelana.view.main.ThemeViewModelFactory
import com.capstonehore.ngelana.view.signup.SignUpActivity
import kotlinx.coroutines.launch
import java.util.Locale

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var themeManager: ThemeManager
    private lateinit var themeViewModel: ThemeViewModel

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userPreferences: UserPreferences

    private val Context.dataStore by preferencesDataStore(THEME_SETTINGS)
    private val Context.sessionDataStore by preferencesDataStore(USER_SESSION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale(this)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = obtainViewModel(this@LoginActivity)
        userPreferences = UserPreferences.getInstance(sessionDataStore)

        setupAction()
        setupImage()
        setupAnimation()
        setupTitle()
        setupButton()
        themeSettings()
    }

    private fun setupAction() {
        binding.submitButton.setOnClickListener { setupLogin() }
    }

    private fun setupImage() {
        val image = "https://storage.googleapis.com/ngelana-bucket/ngelana-assets/img_ngelana10.png"
        Glide.with(this@LoginActivity)
            .load(image)
            .into(binding.imageView)
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.logoImage, View.TRANSLATION_X, -70f, 70f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -5f, 5f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val tvTitle = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(300)
        val tvDescription =
            ObjectAnimator.ofFloat(binding.tvDescription, View.ALPHA, 1f).setDuration(300)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(400)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(300)
        val submitButton =
            ObjectAnimator.ofFloat(binding.submitButton, View.ALPHA, 1f).setDuration(400)
        val tvRegister = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(
                tvTitle,
                tvDescription,
                tvEmail,
                tvPassword,
                submitButton,
                tvRegister
            )
            start()
        }
    }

    private fun setupTitle() {
        val blue = ContextCompat.getColor(this, R.color.blue)

        val spannable =
            SpannableString(getString(R.string.login_title, getString(R.string.app_name)))
        spannable.setSpan(
            ForegroundColorSpan(blue),
            spannable.indexOf(getString(R.string.app_name)),
            spannable.indexOf(getString(R.string.app_name)) + getString(R.string.app_name).length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvTitle.text = spannable
    }

    private fun setupButton() {
        val blue = ContextCompat.getColor(this, R.color.blue)

        val spannable = SpannableString(
            getString(
                R.string.register_button_from_login,
                getString(R.string.sign_up_here)
            )
        )
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            }
        }

        val boldSpan = StyleSpan(Typeface.BOLD)
        spannable.setSpan(
            boldSpan,
            spannable.indexOf(getString(R.string.sign_up_here)),
            spannable.indexOf(getString(R.string.sign_up_here)) + getString(R.string.sign_up_here).length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            clickableSpan,
            spannable.indexOf(getString(R.string.sign_up_here)),
            spannable.indexOf(getString(R.string.sign_up_here)) + getString(R.string.sign_up_here).length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(blue),
            spannable.indexOf(getString(R.string.sign_up_here)),
            spannable.indexOf(getString(R.string.sign_up_here)) + getString(R.string.sign_up_here).length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvRegister.text = spannable
        binding.tvRegister.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setupLogin() {
        val email = binding.edEmail.text.toString()
        val password = binding.edPassword.text.toString()

        when {
            email.isEmpty() -> {
                binding.edEmail.error = getString(R.string.empty_email)
            }
            password.isEmpty() -> {
                binding.edPassword.error = getString(R.string.empty_password)
            }
            else -> {
                loginViewModel.doLogin(email, password).observe(this@LoginActivity){
                    if(it != null){
                        when(it) {
                            is Result.Success -> {
                                showLoading(false)

                                val response = it.data

                                val token = response.token ?: ""
                                val userId = response.data?.id ?: ""

                                loginViewModel.saveLogin(token)
                                loginViewModel.saveUserId(userId)

                                showCustomAlertDialog(true, "")
                                Log.d(TAG, "Success registering: $response")
                            }
                            is Result.Error -> {
                                showLoading(false)

                                val response = it.error
                                showCustomAlertDialog(false, response)
                                Log.e(TAG, "Error login: $response")
                            }
                            is Result.Loading -> {
                                showLoading(true)

                                Log.d(TAG, "Loading Login User ....")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showCustomAlertDialog(isSuccess: Boolean, message: String) {
        val inflater = LayoutInflater.from(this)
        val alertLayout = CustomAlertDialogBinding.inflate(inflater)

        val builder = AlertDialog.Builder(this)
        builder.setView(alertLayout.root)

        val dialog = builder.create()
        dialog.show()

        if (isSuccess) {
            with(alertLayout) {
                alertIcon.setImageResource(R.drawable.ic_check_circle)
                alertTitle.text = getString(R.string.login_success_title)
                alertMessage.text = getString(R.string.login_success_message)

                submitButton.setOnClickListener {
                    lifecycleScope.launch {
                        when (loginViewModel.hasUserPreference()) {
                            true -> moveToMain()
                            false -> moveToInterest()
                        }
                        dialog.dismiss()
                    }
                }
            }
        } else {
            with(alertLayout) {
                alertIcon.setImageResource(R.drawable.ic_error)
                alertTitle.text = getString(R.string.login_failed)
                alertMessage.text = message

                submitButton.apply {
                    text = getString(R.string.cancel)
                    setBackgroundColor(ContextCompat.getColor(this@LoginActivity, R.color.light_grey))
                    setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.black))
                    setOnClickListener {
                        dialog.dismiss()
                    }
                }
            }
        }

        // Animation
        val scaleX = ObjectAnimator.ofFloat(alertLayout.alertIcon, "scaleX", 0.5f, 1f)
        val scaleY = ObjectAnimator.ofFloat(alertLayout.alertIcon, "scaleY", 0.5f, 1f)
        val tvTitle = ObjectAnimator.ofFloat(alertLayout.alertTitle, View.ALPHA, 0f, 1f)
        val tvMessage = ObjectAnimator.ofFloat(alertLayout.alertMessage, View.ALPHA, 0f, 1f)
        val submitButton = ObjectAnimator.ofFloat(alertLayout.submitButton, View.ALPHA, 0f, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, tvTitle, tvMessage, submitButton)
        animatorSet.duration = 800
        animatorSet.start()
    }

    private fun themeSettings() {
        themeManager = ThemeManager.getInstance(dataStore)

        themeViewModel = ViewModelProvider(
            this@LoginActivity,
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

    private fun moveToMain() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    private fun moveToInterest() {
        startActivity(Intent(this@LoginActivity, InterestActivity::class.java))
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
       binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): LoginViewModel {
        val factory = ViewModelFactory.getInstance(
            activity.application,
            userPreferences
        )
        return ViewModelProvider(activity, factory)[LoginViewModel::class.java]
    }

    companion object {
        private const val TAG = "LoginActivity"
        const val THEME_SETTINGS = "theme_settings"
        const val USER_SESSION = "user_session"

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