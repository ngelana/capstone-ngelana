package com.capstonehore.ngelana.view.home.plan.recommendation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.RecommendationPlaceAdapter
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.databinding.ActivityRecommendationPlanBinding
import com.capstonehore.ngelana.utils.withDateFormat
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.detail.DetailPlaceFragment
import com.capstonehore.ngelana.view.explore.place.PlaceViewModel
import com.capstonehore.ngelana.view.home.plan.customize.CustomizePlanActivity
import com.capstonehore.ngelana.view.home.plan.customize.CustomizePlanActivity.Companion.RESULT_CODE

class RecommendationPlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendationPlanBinding

    private var selectedDate: String? = null
    private val placeItem = mutableListOf<PlaceItem>()

    private lateinit var recommendationPlaceAdapter: RecommendationPlaceAdapter

    private lateinit var placeViewModel: PlaceViewModel

    @SuppressLint("NotifyDataSetChanged")
    private val addPlaceLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_CODE && result.data != null) {
            @Suppress("DEPRECATION")
            val returnedPlace = result.data?.getParcelableArrayListExtra<PlaceItem>(EXTRA_RETURN_PLACE)
            Log.d(TAG, "Returned Places: $returnedPlace")
            returnedPlace?.let { places ->
                for (place in places) {
                    if (!placeItem.contains(place)) {
                        placeItem.add(place)
                    }
                }
                recommendationPlaceAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        placeViewModel = obtainViewModel(this@RecommendationPlanActivity)

        setupAction()
        setupToolbar()
        setupDate()
        setupAdapter()
        setupView()
    }

    private fun setupAction() {
        binding.submitButton.setOnClickListener {
            val intent = Intent(this@RecommendationPlanActivity, CustomizePlanActivity::class.java).apply {
                putExtra(CustomizePlanActivity.EXTRA_DATE, selectedDate)
            }
            startActivity(intent)
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

    private fun setupDate() {
        selectedDate = intent.getStringExtra(EXTRA_DATE)
        binding.planDate.text = selectedDate?.withDateFormat() ?: ""
    }

    private fun setupAdapter() {
        recommendationPlaceAdapter = RecommendationPlaceAdapter(placeItem)

        binding.rvPlaces.apply {
            layoutManager = LinearLayoutManager(this@RecommendationPlanActivity)
            adapter = recommendationPlaceAdapter
        }

        recommendationPlaceAdapter.setOnItemClickCallback(object : RecommendationPlaceAdapter.OnItemClickCallback {
            override fun onItemClicked(item: PlaceItem?) {
                item?.let {
                    val dialogFragment = DetailPlaceFragment.newInstance(it)
                    dialogFragment.show(supportFragmentManager, "DetailPlaceFragment")
                }
            }
        })

        recommendationPlaceAdapter.setOnClearButtonClickCallback(object : RecommendationPlaceAdapter.OnClearButtonClickCallback {
            override fun onClearButtonClicked(item: PlaceItem?) {
                item?.let {
                    placeItem.remove(it)
                    recommendationPlaceAdapter.submitList(ArrayList(placeItem))
                }
            }
        })

        recommendationPlaceAdapter.setOnAddButtonClickCallback(object : RecommendationPlaceAdapter.OnAddButtonClickCallback {
            override fun onAddButtonClicked(item: PlaceItem?) {
                item?.let {
                    val intent = Intent(this@RecommendationPlanActivity, CustomizePlanActivity::class.java).apply {
                        putExtra(CustomizePlanActivity.EXTRA_PLACE, it)
                        putExtra(CustomizePlanActivity.EXTRA_DATE, selectedDate)
                    }
                    addPlaceLauncher.launch(intent)

                    placeItem.remove(it)
                    recommendationPlaceAdapter.submitList(ArrayList(placeItem))
                }
            }
        })
    }

    private fun setupView() {
        placeViewModel.getAllPlaces().observe(this) {
            if (it != null) {
                when (it) {
                    is Result.Success -> {
                        showLoading(false)

                        val response = it.data
                        val filteredPlacesWithImages = response.filter { place ->
                            !place.urlPlaceholder.isNullOrEmpty()
                        }

                        if (placeItem.size <= 10) {
                            val remainingItemCount = 10 - placeItem.size
                            val randomPlaces = filteredPlacesWithImages.shuffled().take(remainingItemCount)

                            placeItem.clear()
                            placeItem.addAll(randomPlaces)
                        }

                        recommendationPlaceAdapter.submitList(placeItem)
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): PlaceViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[PlaceViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        binding.rvPlaces.adapter?.notifyDataSetChanged()
    }

    companion object {
        const val TAG = "RecommendationPlanActivity"
        const val EXTRA_DATE = "extra_date"
        const val EXTRA_RETURN_PLACE = "extra_return_place"
    }
}