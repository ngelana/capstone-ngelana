package com.capstonehore.ngelana.view.profile.language

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.LanguageAdapter
import com.capstonehore.ngelana.data.local.entity.Language
import com.capstonehore.ngelana.databinding.ActivityLanguageBinding
import com.capstonehore.ngelana.utils.LanguagePreference
import com.capstonehore.ngelana.view.main.MainActivity.Companion.setLocale
import com.capstonehore.ngelana.view.onboarding.OnboardingActivity

class LanguageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLanguageBinding

    private var delay: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)
        setupToolbar()
        setupView()
    }

    private fun setupToolbar() {
        with(binding) {
            setSupportActionBar(topAppBar)
            topAppBar.setNavigationIcon(R.drawable.ic_arrow_back)
            topAppBar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun getListLanguage(): ArrayList<Language> {
        val dataCode = resources.getStringArray(R.array.data_languages_code)
        val dataName = resources.getStringArray(R.array.data_languages_name)
        val listLanguage = ArrayList<Language>()

        for (i in dataName.indices) {
            val language = Language(dataCode[i], dataName[i])
            listLanguage.add(language)
        }
        return listLanguage
    }

    private fun setupView() {
        val languageList = getListLanguage()
        val languageAdapter = LanguageAdapter(languageList)

        binding.rvLanguage.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@LanguageActivity)
            adapter = languageAdapter
        }

        languageAdapter.setOnItemClickCallback(object : LanguageAdapter.OnItemClickCallback {
            override fun onItemClicked(language: Language) {
                if (language.code == "clear") {
                    LanguagePreference.clearLanguage(this@LanguageActivity)
                    restartApp()
                } else {
                    LanguagePreference.setLanguage(this@LanguageActivity, language.code)
                    setLocale(this@LanguageActivity)
                    restartApp()
                }
            }
        })
    }

    private fun restartApp() {
        showLoading(true)
        window.decorView.postDelayed({
            val intent = Intent(this@LanguageActivity, OnboardingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }, delay)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}