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
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.preferences.ThemeManager
import com.capstonehore.ngelana.databinding.ActivityLoginBinding
import com.capstonehore.ngelana.databinding.CustomAlertDialogBinding
import com.capstonehore.ngelana.utils.LanguagePreference
import com.capstonehore.ngelana.view.main.MainActivity
import com.capstonehore.ngelana.view.main.ThemeViewModel
import com.capstonehore.ngelana.view.main.ThemeViewModelFactory
import com.capstonehore.ngelana.view.signup.SignUpActivity
import java.util.Locale

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var themeManager: ThemeManager
    private lateinit var themeViewModel: ThemeViewModel

    private val Context.dataStore by preferencesDataStore(THEME_SETTINGS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale(this)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            .into(binding.logoImage)
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.logoImage, View.TRANSLATION_X, -30f, 30f).apply {
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
            playSequentially(tvTitle, tvDescription, tvEmail, tvPassword, submitButton, tvRegister)
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
        showCustomAlertDialog(true, "")
    }

    private fun showCustomAlertDialog(isSuccess: Boolean, message: String) {
        val inflater = LayoutInflater.from(this)
        val alertLayout = CustomAlertDialogBinding.inflate(inflater)

        val builder = AlertDialog.Builder(this)
        builder.setView(alertLayout.root)

        val dialog = builder.create()
        dialog.show()

        if (isSuccess) {
            alertLayout.alertIcon.setImageResource(R.drawable.ic_check_circle)
            alertLayout.alertTitle.text = getString(R.string.login_success_title)
            alertLayout.alertMessage.text = getString(R.string.login_success_message)

            alertLayout.positiveButton.setOnClickListener {
                moveToMain()
                dialog.dismiss()
            }
            alertLayout.negativeButton.visibility = View.GONE
        } else {
            alertLayout.alertIcon.setImageResource(R.drawable.ic_error)
            alertLayout.alertTitle.text = getString(R.string.login_failed)
            alertLayout.alertMessage.text = message

            alertLayout.negativeButton.setOnClickListener {
                dialog.dismiss()
            }
            alertLayout.positiveButton.visibility = View.GONE
        }

        // Animation
        val scaleX = ObjectAnimator.ofFloat(alertLayout.alertIcon, "scaleX", 0.5f, 1f)
        val scaleY = ObjectAnimator.ofFloat(alertLayout.alertIcon, "scaleY", 0.5f, 1f)
        val tvTitle = ObjectAnimator.ofFloat(alertLayout.alertTitle, View.ALPHA, 0f, 1f)
        val tvMessage = ObjectAnimator.ofFloat(alertLayout.alertMessage, View.ALPHA, 0f, 1f)
        val positiveButton = ObjectAnimator.ofFloat(alertLayout.positiveButton, View.ALPHA, 0f, 1f)
        val negativeButton = ObjectAnimator.ofFloat(alertLayout.negativeButton, View.ALPHA, 0f, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, tvTitle, tvMessage, positiveButton, negativeButton)
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

    companion object {
        const val THEME_SETTINGS = "theme_settings"

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