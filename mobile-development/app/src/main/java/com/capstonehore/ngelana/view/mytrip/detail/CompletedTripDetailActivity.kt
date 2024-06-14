package com.capstonehore.ngelana.view.mytrip.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.databinding.ActivityCompletedTripDetailBinding
import com.capstonehore.ngelana.view.home.plan.date.DatePlanActivity

class CompletedTripDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompletedTripDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompletedTripDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupToolbar()
    }

    private fun setupAction() {
        binding.startPlanningButton.setOnClickListener {
            startActivity(Intent(this@CompletedTripDetailActivity, DatePlanActivity::class.java))
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
}