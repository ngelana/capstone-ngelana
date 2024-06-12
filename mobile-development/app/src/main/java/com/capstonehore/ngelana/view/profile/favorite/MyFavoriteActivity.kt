package com.capstonehore.ngelana.view.profile.favorite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.databinding.ActivityMyFavoriteBinding

class MyFavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupView()
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

    private fun setupView() {
//        val placeAdapter = PlaceAdapter()
//
//        binding.rvFavorite.apply {
//            setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(this@MyFavoriteActivity)
//            adapter = placeAdapter
//        }
//
//        placeAdapter.setOnItemClickCallback(object : PlaceAdapter.OnItemClickCallback {
//            override fun onItemClicked(items: Place) {
//                val dialogFragment = DetailPlaceFragment.newInstance(items)
//                dialogFragment.show(childFragmentManager, "DetailPlaceFragment")
//            }
//        })
    }
}