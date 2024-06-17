package com.capstonehore.ngelana.view.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.FavoriteAdapter
import com.capstonehore.ngelana.adapter.PlaceAdapter
import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.databinding.FragmentHomeBinding
import com.capstonehore.ngelana.view.detail.DetailPlaceFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

//    private lateinit var homeViewModel: HomeViewModel

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

//        homeViewModel = obtainViewModel(requireActivity())

        setupAction()
        setupAnimation()
        setupTitle()
        setupView()
//        getDetailLocation()
    }

    private fun setupAction() {
        binding.seeMoreFavorite.setOnClickListener {
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

    private fun getListPlace(): ArrayList<Place> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataImage = resources.getStringArray(R.array.data_image)
        val listPlace= ArrayList<Place>()

        for (i in dataName.indices) {
            val place = Place(dataName[i], dataDescription[i], dataImage[i])
            listPlace.add(place)
        }
        return listPlace
    }

    private fun setupView() {
        val favoritePlaceList = getListPlace()
        val recommendationPlaceList = getListPlace()

        val favoritePlaceAdapter = FavoriteAdapter(favoritePlaceList)
        val recommendationPlaceAdapter = PlaceAdapter(recommendationPlaceList)

        binding.rvFavoritePlace.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = favoritePlaceAdapter
        }

        binding.rvRecommendationPlace.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = recommendationPlaceAdapter
        }

        favoritePlaceAdapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(items: Place) {
                val dialogFragment = DetailPlaceFragment.newInstance(items)
                dialogFragment.show(childFragmentManager, "DetailPlaceFragment")
            }
        })

        recommendationPlaceAdapter.setOnItemClickCallback(object : PlaceAdapter.OnItemClickCallback {
            override fun onItemClicked(items: Place) {
                val dialogFragment = DetailPlaceFragment.newInstance(items)
                dialogFragment.show(childFragmentManager, "DetailPlaceFragment")
            }
        })
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

//    private fun obtainViewModel(activity: FragmentActivity): HomeViewModel {
//        val factory = ViewModelFactory.getInstance(
//            requireContext(),
//            UserPreferences.getInstance(requireContext().dataStore)
//        )
//        return ViewModelProvider(activity, factory)[HomeViewModel::class.java]
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}