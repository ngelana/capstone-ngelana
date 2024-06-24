package com.capstonehore.ngelana.view.detail

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.PhotoAdapter
import com.capstonehore.ngelana.adapter.SimilarPlaceAdapter
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.local.entity.Favorite
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.databinding.ActivityDetailPlaceBinding
import com.capstonehore.ngelana.utils.splitAndReplaceCommas
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.explore.place.PlaceViewModel
import com.capstonehore.ngelana.view.profile.favorite.FavoriteViewModel

class DetailPlaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPlaceBinding

    private lateinit var similarPlaceAdapter: SimilarPlaceAdapter
    private var currentLocation: Location? = null

    private lateinit var placeViewModel: PlaceViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SESSION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        placeViewModel = obtainViewModel(this@DetailPlaceActivity)

        @Suppress("DEPRECATION")
        val placeItem = intent.getParcelableExtra<PlaceItem>(EXTRA_PLACES)
        placeItem?.let { item ->
            setupView(item)
            setupDetailPlace(item)
            setupFavorite(item)
        }
    }

    private fun setupToolbar(name: String) {
        with(binding) {
            setSupportActionBar(topAppBar)
            topAppBar.title = name
            topAppBar.setNavigationIcon(R.drawable.ic_arrow_back)
            topAppBar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun setupView(placeItem: PlaceItem?) {
        placeItem?.let { item ->
            val name = item.name ?: getString(R.string.unknown)
            item.id?.let { placeId ->
                setupPlace(placeId)
                setupToolbar(name)
            } ?: run {
                binding.tvNoData.visibility = View.VISIBLE
            }
        }
    }

    private fun setupPlace(placeId: String) {
        placeViewModel.getPlaceById(placeId).observe(this@DetailPlaceActivity) {
            if (it != null) {
                when (it) {
                    is Result.Success -> {
                        showLoading(false)

                        val response = it.data
                        Log.d(TAG, "Successfully Show Detail of Place: $response")
                    }
                    is Result.Error -> {
                        showLoading(false)

                        showToast(it.error)
                        Log.d(TAG, "Failed to Show Detail of Place: ${it.error}")
                    }
                    is Result.Loading -> showLoading(true)
                }
            }
        }
    }

    private fun setupDetailPlace(items: PlaceItem?) {
        items?.let { item ->
            binding.apply {
                val placeImage = item.urlPlaceholder ?: emptyList()
                val primaryType = item.primaryTypes?.splitAndReplaceCommas()
                val typesList = item.types?.splitAndReplaceCommas()

                setupImageAdapter(placeImage)
                setupLocation()
//                clearCircleViews()
//                addCircleViews(item)
                setupSimilarAdapter()
                setupSimilarPlace()

                placeName.text = item.name
                placePrimaryType.text = primaryType?.joinToString(", ")
                placeType.text = typesList?.joinToString(", ")
                placeRating.text = item.rating.toString()
                placeRatingCount.text = item.ratingCount.toString()
                placeStatus.text = item.status
                placeAddress.text = item.address
                placePhone.text = item.phone
            }
        }
    }

    private fun setupImageAdapter(item: List<String>) {
        val photoAdapter = PhotoAdapter(item)

        binding.rvPlaceImage.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(this@DetailPlaceActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = photoAdapter
        }
    }

    private fun setupLocation(): String {
        var placeCity = getString(R.string.unknown)

        currentLocation?.let { location ->
            placeViewModel.getLocationDetails(this, location)

            placeViewModel.locationResult.observe(this) {
                if (it != null) {
                    when (it) {
                        is Result.Success -> {
                            val response = it.data
                            placeCity = response.locality ?: getString(R.string.unknown)
                            binding.placeCity.text = placeCity
                        }
                        is Result.Error -> {
                            binding.placeCity.text = getString(R.string.unknown)
                            Log.e(TAG, "Failed to get location details: ${it.error}")
                        }
                        is Result.Loading -> {}
                    }
                }
            }
        }

        return placeCity
    }

//    private fun clearCircleViews() {
//        binding.constraintLayout.removeViews(
//            binding.constraintLayout.indexOfChild(binding.placeType) + 1,
//            binding.constraintLayout.childCount - 1
//        )
//    }

//    private fun addCircleViews(place: PlaceItem) {
//        place.types?.forEachIndexed { _, _ ->
//            val circleView = View(this)
//            circleView.id = View.generateViewId()
//            circleView.background = ContextCompat.getDrawable(this, R.drawable.circle)
//
//            binding.constraintLayout.addView(circleView)
//
//            val constraintSet = ConstraintSet()
//            constraintSet.clone(binding.constraintLayout)
//
//            constraintSet.connect(
//                circleView.id, ConstraintSet.START,
//                R.id.placeType, ConstraintSet.END, 8
//            )
//            constraintSet.connect(
//                circleView.id, ConstraintSet.TOP,
//                R.id.placeType, ConstraintSet.TOP, 8
//            )
//            constraintSet.connect(
//                circleView.id, ConstraintSet.BOTTOM,
//                R.id.placeType, ConstraintSet.BOTTOM, 8
//            )
//
//            constraintSet.applyTo(binding.constraintLayout)
//        }
//    }

    private fun setupSimilarAdapter() {
        similarPlaceAdapter = SimilarPlaceAdapter()

        binding.rvSimilarPlace.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DetailPlaceActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = similarPlaceAdapter
        }

        similarPlaceAdapter.setOnItemClickCallback(object : SimilarPlaceAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PlaceItem?) {
                data?.let { placeItem ->
                    val currentActivity = this@DetailPlaceActivity
                    currentActivity.updateView(placeItem)
                }
            }
        })
    }

    private fun setupSimilarPlace() {
        placeViewModel.getAllPlaces().observe(this@DetailPlaceActivity) {
            if (it != null) {
                when (it) {
                    is Result.Success -> {
                        showLoading(false)

                        val response = it.data
                        response.let { item ->
                            val randomPlacesWithFiltering = getRandomPlaces(item)
                            similarPlaceAdapter.submitList(randomPlacesWithFiltering)
                        }
                        Log.d(TAG, "Successfully Show Similar Place: $response")
                    }
                    is Result.Error -> {
                        showLoading(false)

                        showToast(it.error)
                        Log.d(TAG, "Failed to Show Similar Place: ${it.error}")
                    }
                    is Result.Loading -> showLoading(true)
                }
            }
        }
    }

    private fun setupFavorite(placeItem: PlaceItem?) {
        val factory = ViewModelFactory.getInstance(
            this,
            UserPreferences.getInstance(dataStore)
        )
        favoriteViewModel = ViewModelProvider(
            this,
            factory
        )[FavoriteViewModel::class.java]

        placeItem?.let { item ->
            val randomIndex = item.urlPlaceholder?.indices?.random()

            item.id?.let { placeId ->
                val placeName = item.name
                val placeImage = item.urlPlaceholder?.get(randomIndex ?: 0)
                val placeCity = setupLocation()
                val placeRating = item.rating.toString()
                val typesList = item.types?.split(", ") ?: emptyList()
                val placeType = typesList.joinToString(", ")

                val favorite = Favorite(
                    placeId,
                    placeName,
                    placeImage,
                    placeCity,
                    placeRating,
                    placeType
                )

                var isFavorite = false

                favoriteViewModel.getFavoriteByPlaceId(placeId).observe(this) {
                    isFavorite = it is Result.Success && it.data.isNotEmpty()
                    setIcon(isFavorite)
                }

                binding.favoriteButton.setOnClickListener {
                    if (!isFavorite) {
                        favoriteViewModel.insertFavoritePlace(favorite)
                        showToast("Successfully added $placeName to Favorite!")
                    } else {
                        favoriteViewModel.deleteFavoritePlace(favorite)
                        showToast("Successfully deleted $placeName from favorite users.")
                    }
                    isFavorite = !isFavorite
                    setIcon(isFavorite)
                }
            }
        }
    }

    private fun setIcon(isFavorite: Boolean) {
        binding.favoriteButton.setImageResource(
            if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        )
    }

    private fun filterHighRatingPlaces(response: List<PlaceItem>): List<PlaceItem> {
        return response.filter { item ->
            (item.rating ?: 0.0) > 5.0
        }
    }

    private fun getRandomPlaces(response: List<PlaceItem>): List<PlaceItem> {
        val highRatingPlaces = filterHighRatingPlaces(response)

        return if (highRatingPlaces.size > 8) highRatingPlaces.shuffled().take(8) else highRatingPlaces
    }

    fun updateView(placeItem: PlaceItem?) {
        setupView(placeItem)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): PlaceViewModel {
        val factory = ViewModelFactory.getInstance(
            activity.application,
            UserPreferences.getInstance(dataStore)
        )
        return ViewModelProvider(activity, factory)[PlaceViewModel::class.java]
    }

    companion object {
        private const val TAG = "DetailPlaceActivity"
        const val EXTRA_PLACES= "extra_places"
        const val SESSION = "session"
    }
}