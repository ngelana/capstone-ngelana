package com.capstonehore.ngelana.view.profile.interest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstonehore.ngelana.databinding.ActivityInterestBinding

class MyInterestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInterestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterestBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}