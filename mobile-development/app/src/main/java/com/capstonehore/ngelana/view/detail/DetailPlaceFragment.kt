package com.capstonehore.ngelana.view.detail

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.PhotoAdapter
import com.capstonehore.ngelana.adapter.SimilarPlaceAdapter
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.local.entity.Favorite
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.databinding.FragmentDetailPlaceBinding
import com.capstonehore.ngelana.utils.obtainViewModel
import com.capstonehore.ngelana.view.explore.place.PlaceViewModel
import com.capstonehore.ngelana.view.profile.favorite.FavoriteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailPlaceFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentDetailPlaceBinding? = null

    private val binding get() = _binding!!

    private lateinit var similarPlaceAdapter: SimilarPlaceAdapter
    private var currentLocation: Location? = null

    private lateinit var placeViewModel: PlaceViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel

    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        placeViewModel = obtainViewModel(PlaceViewModel::class.java) as PlaceViewModel

        @Suppress("DEPRECATION")
        val placeItem = arguments?.getParcelable<PlaceItem>(ARG_PLACE)
        placeItem?.let { item ->
            setupView(item)
            setupFavorite(item)
        }
    }

    private fun setupView(placeItem: PlaceItem?) {
        placeItem?.let { item ->
            item.id?.let { placeId ->
                setupPlace(placeId)
            } ?: run {
                showToast(getString(R.string.not_available))
                dismiss()
            }
        }
    }

    private fun setupPlace(placeId: String) {
        placeViewModel.getPlaceById(placeId).observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Result.Success -> {
                        showLoading(false)

                        val response = it.data
                        setupDetailPlace(response)
                        Log.d(TAG, "Successfully Show Detail of Place: $response")
                    }
                    is Result.Error -> {
                        showLoading(false)

                        showToast(it.error)
                        Log.d(TAG, "Failed to Show Detail of Place: ${it.error}")
                        dismiss()
                    }
                    is Result.Loading -> showLoading(true)
                }
            }
        }
    }

    private fun setupDetailPlace(item: PlaceItem) {
        binding.apply {
            val placeImage = item.urlPlaceholder ?: emptyList()
            setupImageAdapter(placeImage)
            setupLocation()
            clearCircleViews()
            addCircleViews(item)
            setupSimilarAdapter()
            setupSimilarPlace()

            placeName.text = item.name
            placePrimaryType.text = item.primaryTypes
            placeType.text = item.types?.joinToString(", ") { it }
            placeRating.text = item.rating.toString()
            placeRatingCount.text = item.ratingCount.toString()
            placeStatus.text = item.status
            placeAddress.text = item.address
            placePhone.text = item.phone
        }
    }

    private fun setupImageAdapter(item: List<String>) {
        val photoAdapter = PhotoAdapter(item)

        binding.rvPlaceImage.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = photoAdapter
        }
    }

    private fun setupLocation(): String {
        var placeCity = getString(R.string.unknown)

        currentLocation?.let { location ->
            placeViewModel.getLocationDetails(requireContext(), location)

            placeViewModel.locationResult.observe(viewLifecycleOwner) {
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

    private fun clearCircleViews() {
        binding.constraintLayout.removeViews(
            binding.constraintLayout.indexOfChild(binding.placeType) + 1,
            binding.constraintLayout.childCount - 1
        )
    }

    private fun addCircleViews(place: PlaceItem) {
        place.types?.forEachIndexed { _, _ ->
            val circleView = View(requireContext())
            circleView.id = View.generateViewId()
            circleView.background = ContextCompat.getDrawable(requireContext(), R.drawable.circle)

            binding.constraintLayout.addView(circleView)

            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.constraintLayout)

            constraintSet.connect(
                circleView.id, ConstraintSet.START,
                R.id.placeType, ConstraintSet.END, 8
            )
            constraintSet.connect(
                circleView.id, ConstraintSet.TOP,
                R.id.placeType, ConstraintSet.TOP, 8
            )
            constraintSet.connect(
                circleView.id, ConstraintSet.BOTTOM,
                R.id.placeType, ConstraintSet.BOTTOM, 8
            )

            constraintSet.applyTo(binding.constraintLayout)
        }
    }

    private fun setupSimilarAdapter() {
        similarPlaceAdapter = SimilarPlaceAdapter()

        binding.rvSimilarPlace.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = similarPlaceAdapter
        }

        similarPlaceAdapter.setOnItemClickCallback(object : SimilarPlaceAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PlaceItem?) {
                data?.let {
                    val intent = Intent(requireActivity(), DetailPlaceActivity::class.java).apply {
                        putExtra(DetailPlaceActivity.EXTRA_PLACES, data)
                    }
                    startActivity(intent)
                }
            }
        })
    }

    private fun setupSimilarPlace() {
        placeViewModel.getAllPlaces().observe(viewLifecycleOwner) {
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
        favoriteViewModel = obtainViewModel(FavoriteViewModel::class.java) as FavoriteViewModel

        placeItem?.let { item ->
            val randomIndex = item.urlPlaceholder?.indices?.random()

            item.id?.let { placeId ->
                val placeName = item.name
                val placeImage = item.urlPlaceholder?.get(randomIndex ?: 0)
                val placeCity = setupLocation()
                val placeRating = item.rating.toString()
                val placeType = item.types ?: emptyList()

                val favorite = Favorite(
                    placeId,
                    placeName,
                    placeImage,
                    placeCity,
                    placeRating,
                    placeType.joinToString(", ") // Join the list into a comma-separated string
                )

                favoriteViewModel.getFavoriteByPlaceId(placeId).observe(viewLifecycleOwner) {
                    isFavorite = it != null
                    setIcon()
                }

                binding.favoriteButton.setOnClickListener {
                    when {
                        !isFavorite -> {
                            favoriteViewModel.insertFavoritePlace(favorite)
                            showToast("Successfully added $placeName to Favorite!")
                        }
                        else -> {
                            favoriteViewModel.deleteFavoritePlace(favorite)
                            showToast("Successfully deleted $placeName from favorite users.")
                        }
                    }
                    isFavorite = !isFavorite
                    setIcon()
                }
            }
        }
    }

    private fun setIcon() {
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

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading:Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "DetailPlaceFragment"
        private const val ARG_PLACE = "arg_place"

        fun newInstance(placeItem: PlaceItem): DetailPlaceFragment {
            return DetailPlaceFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PLACE, placeItem)
                }
            }
        }
    }
}