package com.capstonehore.ngelana.view.login.interest

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.InterestAdapter
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.remote.response.PreferenceItem
import com.capstonehore.ngelana.data.remote.response.preferences.UserDataPreferencesItem
import com.capstonehore.ngelana.databinding.ActivityInterestBinding
import com.capstonehore.ngelana.utils.obtainViewModel
import com.capstonehore.ngelana.view.main.MainActivity
import com.capstonehore.ngelana.view.profile.interest.InterestViewModel

class InterestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInterestBinding

    private lateinit var interestAdapter: InterestAdapter

    private val selectedItems = SparseBooleanArray()
    private var interestCount: Int = 0

    private lateinit var interestViewModel: InterestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        interestViewModel = obtainViewModel(InterestViewModel::class.java) as InterestViewModel

        setupAction()
        setupAnimation()
        setupAdapter()
        setupView()
    }

    private fun setupAction() {
        binding.skipButton.setOnClickListener {
            startActivity(Intent(this@InterestActivity, MainActivity::class.java))
            finish()
        }
        binding.nextButton.setOnClickListener {
            setupInterest()
        }
    }

    private fun setupAnimation() {
        val tvTitle = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(300)
        val tvDescription =
            ObjectAnimator.ofFloat(binding.tvDescription, View.ALPHA, 1f).setDuration(300)
        val rvInterest = ObjectAnimator.ofFloat(binding.rvInterest, View.ALPHA, 1f).setDuration(300)
        val nextButton =
            ObjectAnimator.ofFloat(binding.nextButton, View.ALPHA, 1f).setDuration(300)
        val tvInterestCount =
            ObjectAnimator.ofFloat(binding.tvInterestCount, View.ALPHA, 1f).setDuration(300)
        val skipButton = ObjectAnimator.ofFloat(binding.skipButton, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(nextButton, tvInterestCount, skipButton)
        }

        AnimatorSet().apply {
            playSequentially(
                tvTitle,
                tvDescription,
                rvInterest,
                together
            )
            start()
        }
    }

    private fun setupAdapter() {
        interestAdapter = InterestAdapter(selectedItems).apply {
            setOnItemClickCallback(object : InterestAdapter.OnItemClickCallback {
                override fun onItemClicked(data: PreferenceItem?) {
                    val position = interestAdapter.currentList.indexOf(data)
                    if (position != -1) {
                        when (selectedItems[position]) {
                            true -> selectedItems.delete(position)
                            false -> selectedItems.put(position, true)
                        }
                        updateInterestCount()
                        interestAdapter.notifyItemChanged(position)
                    }
                }
            })
        }

        binding.rvInterest.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@InterestActivity, 2)
            adapter = interestAdapter
        }
    }

    private fun setupView() {
        interestViewModel.getAllPreferences().observe(this) {
            if (it != null) {
                when (it) {
                    is Result.Success -> {
                        showLoading(false)

                        val response = it.data
                        response.let { item ->
                            interestAdapter.submitList(item)
                        }
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

    private fun setupInterest() {
        val userId = interestViewModel.getUserId().toString()
        val selectedPreferences = interestAdapter.currentList.filterIndexed { index, _ ->
            selectedItems[index]
        }.map { preferenceItem ->
            UserDataPreferencesItem(userId = userId, preference = preferenceItem)
        }

        interestViewModel.createUserPreference(selectedPreferences).observe(this) {
            when (it) {
                is Result.Success -> {
                    showLoading(false)

                    val response = it.data

                    showToast(getString(R.string.successfully_saved_preferences))
                    Log.d(TAG, "Successfully created preferences: $response")
                }
                is Result.Error -> {
                    showLoading(false)

                    showToast(it.error)
                    Log.d(TAG, "Failed to create preferences: ${it.error}")
                }
                is Result.Loading -> showLoading(true)
            }
        }
    }

    private fun updateInterestCount() {
        interestCount = selectedItems.size()
        binding.tvInterestCount.text = interestCount.toString()

        if (binding.tvInterestCount.alpha == 0f) {
            binding.tvInterestCount.alpha = 1f
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "InterestActivity"
    }

}