package com.capstonehore.ngelana.view.mytrip.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstonehore.ngelana.databinding.ActivityCompletedTripDetailBinding

class CompletedTripDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompletedTripDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompletedTripDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}