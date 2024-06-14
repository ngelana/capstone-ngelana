package com.capstonehore.ngelana.view.profile.interest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.databinding.ActivityMyInterestBinding

class MyInterestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyInterestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInterestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupToolbar()
        setupView()
    }

    private fun setupAction() {
        binding.addInterest.setOnClickListener {

        }
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

    private fun setupView() {
//        val languageList = getListLanguage()
//        val languageAdapter = LanguageAdapter(languageList)
//
//        binding.rvLanguage.apply {
//            setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(this@LanguageActivity)
//            adapter = languageAdapter
//        }
//
//        languageAdapter.setOnItemClickCallback(object : LanguageAdapter.OnItemClickCallback {
//            override fun onItemClicked(language: Language) {
//                LanguagePreference.setLanguage(this@LanguageActivity, language.code)
//                MainActivity.updateResources(this@LanguageActivity, language.code)
//
//                finish()
//                startActivity(intent)
//            }
//        })
    }

}