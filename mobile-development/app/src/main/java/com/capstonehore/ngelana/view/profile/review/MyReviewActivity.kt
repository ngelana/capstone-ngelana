package com.capstonehore.ngelana.view.profile.review

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstonehore.ngelana.databinding.ActivityMyReviewBinding

class MyReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}