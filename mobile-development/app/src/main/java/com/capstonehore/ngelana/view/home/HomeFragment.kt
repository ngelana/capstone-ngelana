package com.capstonehore.ngelana.view.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.PlaceAdapter
import com.capstonehore.ngelana.adapter.PopularAdapter
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.databinding.FragmentHomeBinding
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.detail.DetailPlaceFragment
import com.capstonehore.ngelana.view.explore.place.PlaceViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var popularAdapter: PopularAdapter
    private lateinit var placeAdapter: PlaceAdapter

    private lateinit var placeViewModel: PlaceViewModel

    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        placeViewModel = obtainViewModel(requireActivity())

        setupAction()
        setupAnimation()
        setupTitle()
        setupAdapter()
        setupView()
//        getDetailLocation()
    }

    private fun setupAction() {
        binding.seeMorePopular.setOnClickListener {
            navController.navigate(R.id.action_navigation_home_to_navigation_explore)
        }

        binding.seeMoreRecommendation.setOnClickListener {
            navController.navigate(R.id.action_navigation_home_to_navigation_explore)
        }

        binding.submitButton.setOnClickListener {
            navController.navigate(R.id.action_navigation_home_to_navigation_plan)
        }
    }

    private fun setupAnimation() {
        val tvTitle = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(300)
        val tvDescription =
            ObjectAnimator.ofFloat(binding.tvDescription, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(tvTitle, tvDescription)
            start()
        }
    }

    private fun setupTitle() {
        val blue = ContextCompat.getColor(requireContext(), R.color.blue)

        val spannable =
            SpannableString(getString(R.string.home_title, getString(R.string.plan)))
        spannable.setSpan(
            ForegroundColorSpan(blue),
            spannable.indexOf(getString(R.string.plan)),
            spannable.indexOf(getString(R.string.plan)) + getString(R.string.plan).length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvTitle.text = spannable
    }

    private fun setupAdapter() {
        popularAdapter = PopularAdapter()
        placeAdapter = PlaceAdapter()

        binding.rvPopularPlace.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = popularAdapter
        }

        binding.rvRecommendationPlace.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = placeAdapter
        }

        popularAdapter.setOnItemClickCallback(object : PopularAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PlaceItem?) {
                data?.let {
                    val dialogFragment = DetailPlaceFragment.newInstance(it)
                    dialogFragment.show(childFragmentManager, "DetailPlaceFragment")
                }
            }
        })

        placeAdapter.setOnItemClickCallback(object : PlaceAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PlaceItem?) {
                data?.let {
                    val dialogFragment = DetailPlaceFragment.newInstance(it)
                    dialogFragment.show(childFragmentManager, "DetailPlaceFragment")
                }
            }
        })
    }

    private fun setupView() {
        placeViewModel.getAllPlaces().observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Result.Success -> {
                        showLoading(false)

                        val response = it.data
                        response.let {
                            val filteredPlacesWithImages = response.filter { place ->
                                !place.urlPlaceholder.isNullOrEmpty()
                            }

                            val randomPlacesWithFiltering = getRandomPlaces(filteredPlacesWithImages)
                            val randomPlacesWithoutFiltering = filteredPlacesWithImages.shuffled().take(8)

                            Log.d(TAG, "Filtered Places: $randomPlacesWithFiltering")
                            Log.d(TAG, "Shuffled Places: $randomPlacesWithoutFiltering")

                            popularAdapter.submitList(randomPlacesWithFiltering)
                            placeAdapter.submitList(randomPlacesWithoutFiltering)
                        }
                        Log.d(TAG, "Successfully Show All Places: $response")
                    }
                    is Result.Error -> {
                        showLoading(false)

                        showToast(it.error)
                        Log.d(TAG, "Failed to Show All Places: ${it.error}")
                    }
                    is Result.Loading -> showLoading(true)
                }
            }
        }
    }

    private fun filterHighRatingPlaces(items: List<PlaceItem>): List<PlaceItem> {
        return items.filter { item ->
            (item.rating ?: 0.0) == 5.0
        }
    }

    private fun getRandomPlaces(response: List<PlaceItem>): List<PlaceItem> {
        val highRatingPlaces = filterHighRatingPlaces(response)

        return if (highRatingPlaces.size > 8) highRatingPlaces.shuffled().take(8) else highRatingPlaces
    }

//    private fun updateLocationUI(location: Location) {
//        homeViewModel.updateLocationUI(requireContext(), location)
//        moveToDatePlan()
//    }
//
//    private fun getDetailLocation() {
//        homeViewModel.locationResult.observe(viewLifecycleOwner, Observer { result ->
//            result?.let {
//                when (it) {
//                    is Result.Loading -> {
//                        showLoading(true)
//                    }
//                    is Result.Success -> {
//                        showLoading(false)
//
//                        val response = it.data
//                        val subLocality = response.subLocality ?: "Unknown"
//                        val locality = response.locality ?: "Unknown"
//                        val locationText = "$subLocality, $locality"
//                        binding.tvLocation.text = locationText
//                    }
//                    is Result.Error -> {
//                        showLoading(false)
//                        showToast("Failed to get location details: ${it.error}")
//                    }
//                }
//            }
//        })
//    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: FragmentActivity): PlaceViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[PlaceViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}