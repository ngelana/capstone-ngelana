package com.capstonehore.ngelana.view.profile.favorite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstonehore.ngelana.databinding.ActivityMyFavoriteBinding

class MyFavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}