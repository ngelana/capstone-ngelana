package com.capstonehore.ngelana.view.detail

import android.content.Context
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
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.PhotoAdapter
import com.capstonehore.ngelana.adapter.SimilarPlaceAdapter
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.local.entity.Favorite
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.databinding.FragmentDetailPlaceBinding
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.explore.place.PlaceViewModel
import com.capstonehore.ngelana.view.profile.favorite.MyFavoriteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailPlaceFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentDetailPlaceBinding? = null

    private val binding get() = _binding!!

    private var currentLocation: Location? = null
    private lateinit var similarPlaceAdapter: SimilarPlaceAdapter

    private lateinit var placeViewModel: PlaceViewModel

    private lateinit var myFavoriteViewModel: MyFavoriteViewModel
    private lateinit var favorite: Favorite
    private var isFavorite = false

    private val Context.sessionDataStore by preferencesDataStore(USER_SESSION)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        placeViewModel = obtainViewModel(requireActivity())

        @Suppress("DEPRECATION")
        val placeItem = arguments?.getParcelable<PlaceItem>(ARG_PLACE)
        placeItem?.id?.let { placeId ->
            setupPlace(placeId)
        } ?: run {
            showToast(getString(R.string.not_available))
            dismiss()
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
                    }
                    is Result.Error -> {
                        showLoading(false)

                        showToast(it.error)
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
            setupPhoto(placeImage)
            setupLocationObserver()
            clearCircleViews()
            addCircleViews(item)
            setupAdapter()

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

    private fun setIcon() {
        binding.favoriteButton.setImageResource(
            if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        )
    }

    private fun setupPhoto(item: List<String>) {
        val photoAdapter = PhotoAdapter(item)

        binding.rvPhoto.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = photoAdapter
        }
    }

    private fun setupLocationObserver() {
        currentLocation?.let { location ->
            placeViewModel.getLocationDetails(requireContext(), location)

            placeViewModel.locationResult.observe(viewLifecycleOwner) {
                if (it != null) {
                    when (it) {
                        is Result.Success -> {
                            val response = it.data
                            binding.placeCity.text = response.locality ?: getString(R.string.unknown)
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

    private fun setupAdapter() {
        similarPlaceAdapter = SimilarPlaceAdapter()

        binding.rvSimilarPlace.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = similarPlaceAdapter
        }

        similarPlaceAdapter.setOnItemClickCallback(object : SimilarPlaceAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PlaceItem?) {
//                data?.let {
//                    val dialogFragment = DetailPlaceFragment.newInstance(it)
//                    dialogFragment.show(supportFragmentManager, "DetailPlaceFragment")
//                }
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading:Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: FragmentActivity): PlaceViewModel {
        val factory = ViewModelFactory.getInstance(
            requireContext(),
            UserPreferences.getInstance(activity.sessionDataStore)
        )
        return ViewModelProvider(activity, factory)[PlaceViewModel::class.java]
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
        const val USER_SESSION = "user_session"

        fun newInstance(placeItem: PlaceItem): DetailPlaceFragment {
            return DetailPlaceFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PLACE, placeItem)
                }
            }
        }
    }
}