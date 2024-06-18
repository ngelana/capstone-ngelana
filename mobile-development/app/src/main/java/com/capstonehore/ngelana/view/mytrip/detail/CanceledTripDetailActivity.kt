package com.capstonehore.ngelana.view.mytrip.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.databinding.ActivityCanceledTripDetailBinding
import com.capstonehore.ngelana.view.main.MainActivity

class CanceledTripDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCanceledTripDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCanceledTripDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupToolbar()
    }

    private fun setupAction() {
        binding.startPlanningButton.setOnClickListener {
            startActivity(Intent(this@CanceledTripDetailActivity, MainActivity::class.java))
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