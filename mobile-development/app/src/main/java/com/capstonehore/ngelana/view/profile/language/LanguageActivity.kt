package com.capstonehore.ngelana.view.profile.language

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.LanguageAdapter
import com.capstonehore.ngelana.data.local.entity.Language
import com.capstonehore.ngelana.databinding.ActivityLanguageBinding
import com.capstonehore.ngelana.utils.LanguagePreference
import com.capstonehore.ngelana.view.main.MainActivity

class LanguageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLanguageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                LanguagePreference.setLanguage(this@LanguageActivity, language.code)
                MainActivity.updateResources(this@LanguageActivity, language.code)

                finish()
                startActivity(intent)
            }
        })
    }

}