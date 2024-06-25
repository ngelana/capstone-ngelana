package com.capstonehore.ngelana.view.mytrip.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.databinding.ActivityUpcomingTripDetailBinding

class UpcomingTripDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpcomingTripDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpcomingTripDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
    }

    private fun setupAction() {
        binding.canceledTripButton.setOnClickListener {

        }
    }

    private fun setupToolbar() {
        with(binding) {
            setSupportActionBar(topAppBar)
            topAppBar.title = binding.planName.text
            topAppBar.setNavigationIcon(R.drawable.ic_arrow_back)
            topAppBar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    companion object {
        private const val TAG = "UpcomingTripDetailActivity"
        const val EXTRA_PLAN_ITEM = "extra_plan_item"
        const val EXTRA_PLAN_NAME = "extra_plan_name"
        const val EXTRA_PLAN_DATE = "extra_plan_date"
    }
}