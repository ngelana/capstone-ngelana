package com.capstonehore.ngelana.view.profile.interest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.MyInterestAdapter
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.remote.response.PreferenceItem
import com.capstonehore.ngelana.databinding.ActivityMyInterestBinding
import com.capstonehore.ngelana.utils.obtainViewModel
import com.capstonehore.ngelana.view.profile.interest.edit.EditMyInterestActivity

class MyInterestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyInterestBinding

    private lateinit var myInterestAdapter: MyInterestAdapter

    private lateinit var interestViewModel: InterestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInterestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        interestViewModel = obtainViewModel(InterestViewModel::class.java) as InterestViewModel

        setupAction()
        setupToolbar()
        setupAdapter()
        setupView()
    }

    private fun setupAction() {
        binding.addInterest.setOnClickListener {
            startActivity(Intent(this@MyInterestActivity, EditMyInterestActivity::class.java))
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

    private fun setupAdapter() {
        myInterestAdapter = MyInterestAdapter()

        binding.rvInterest.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MyInterestActivity)
            adapter = myInterestAdapter
        }

        myInterestAdapter.setOnItemClickCallback(object : MyInterestAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PreferenceItem?) {
                startActivity(Intent(this@MyInterestActivity, EditMyInterestActivity::class.java))
            }
        })
    }

    private fun setupView() {
        interestViewModel.getPreferenceById().observe(this) {
            if (it != null) {
                when (it) {
                    is Result.Success -> {
                        showLoading(false)

                        val response = it.data
                            myInterestAdapter.submitList(response)

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

    companion object {
        private const val TAG = "InterestActivity"
    }

}