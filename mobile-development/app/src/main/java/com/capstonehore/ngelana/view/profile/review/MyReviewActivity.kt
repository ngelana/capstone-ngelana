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

        setupView()
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