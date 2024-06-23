package com.capstonehore.ngelana.view.profile.review

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.ReviewAdapter
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.retrofit.ApiConfig
import com.capstonehore.ngelana.databinding.ActivityMyReviewBinding
import com.capstonehore.ngelana.view.ViewModelFactory

class MyReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyReviewBinding

    private lateinit var reviewAdapter: ReviewAdapter

    private lateinit var reviewViewModel: ReviewViewModel

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SESSION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reviewViewModel = obtainViewModel(this@MyReviewActivity)

        setupToolbar()
        setupAdapter()
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

    private fun setupAdapter() {
        val userPreferences = UserPreferences.getInstance(this.dataStore)
        val token = userPreferences.getToken().toString()

        token.let {
            reviewAdapter = ReviewAdapter(ApiConfig.getApiService(it))

            binding.rvReview.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@MyReviewActivity)
                adapter = reviewAdapter
            }
        }
    }

    private fun setupView() {
        reviewViewModel.getAllReviewByUserId().observe(this) {
            if (it != null) {
                when (it) {
                    is Result.Success -> {
                        showLoading(false)

                        val response = it.data
                        reviewAdapter.submitList(response)

                        Log.d(TAG, "Successfully Show All Preferences: $response")
                    }
                    is Result.Error -> {
                        showLoading(false)

                        showToast(it.error)
                        Log.d(TAG, "Failed to Show All Preferences: ${it.error}")
                    }
                    is Result.Loading -> showLoading(true)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): ReviewViewModel {
        val factory = ViewModelFactory.getInstance(
            activity.application,
            UserPreferences.getInstance(dataStore)
        )
        return ViewModelProvider(activity, factory)[ReviewViewModel::class.java]
    }

    companion object {
        private const val TAG = "MyReviewActivity"
        const val SESSION = "session"
    }
}