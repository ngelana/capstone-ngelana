package com.capstonehore.ngelana.view.profile.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.PlaceAdapter
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.databinding.ActivityMyFavoriteBinding
import com.capstonehore.ngelana.utils.obtainViewModel
import com.capstonehore.ngelana.view.detail.DetailPlaceFragment
import com.capstonehore.ngelana.view.explore.place.PlaceViewModel
import com.capstonehore.ngelana.view.main.MainActivity

class MyFavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyFavoriteBinding

    private lateinit var placeViewModel: PlaceViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel

    private lateinit var placeAdapter: PlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteViewModel = obtainViewModel(FavoriteViewModel::class.java) as FavoriteViewModel

        setupAction()
        setupToolbar()
        setupAdapter()
        setupView()
    }

    private fun setupAction() {
        binding.addFavorite.setOnClickListener {
            startActivity(Intent(this@MyFavoriteActivity, MainActivity::class.java))
        }
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

    private fun setupAdapter() {
        placeAdapter = PlaceAdapter(placeViewModel)

        binding.rvFavorite.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MyFavoriteActivity)
            adapter = placeAdapter
        }

        placeAdapter.setOnItemClickCallback(object : PlaceAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PlaceItem?) {
                data?.let {
                    val dialogFragment = DetailPlaceFragment.newInstance(it)
                    dialogFragment.show(supportFragmentManager, "DetailPlaceFragment")
                }
            }
        })
    }

    private fun setupView() {
        favoriteViewModel.getAllFavorites().observe(this@MyFavoriteActivity) {
            when (it) {
                is Result.Success -> {
                    showLoading(false)

                    val response = it.data
                    val items = response.map { favorite ->
                        PlaceItem(
                            id = favorite.placeId,
                            name = favorite.placeName,
                            urlPlaceholder = listOf(favorite.placeImage ?: ""),
                            address = favorite.placeCity,
                            rating = favorite.placeRating?.toDoubleOrNull() ?: 0.0,
                            types = favorite.placeType?.split(", ") ?: emptyList()
                        )
                    }
                    placeAdapter.submitList(items)
                }
                is Result.Error -> {
                    showLoading(false)
                    showToast(it.error)
                }
                is Result.Loading -> showLoading(true)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}