package com.capstonehore.ngelana.view.mytrip.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstonehore.ngelana.databinding.ActivityUpcomingTripDetailBinding

class UpcomingTripDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpcomingTripDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpcomingTripDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}