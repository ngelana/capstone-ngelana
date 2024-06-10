package com.capstonehore.ngelana.view.mytrip.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstonehore.ngelana.databinding.ActivityCanceledTripDetailBinding

class CanceledTripDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCanceledTripDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCanceledTripDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}