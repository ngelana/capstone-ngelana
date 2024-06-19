package com.capstonehore.ngelana.view.home.plan.recommendation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.RecommendationPlaceAdapter
import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.databinding.ActivityRecommendationPlanBinding
import com.capstonehore.ngelana.utils.withDateFormat
import com.capstonehore.ngelana.view.explore.place.PlaceViewModel
import com.capstonehore.ngelana.view.home.plan.customize.CustomizePlanActivity

class RecommendationPlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendationPlanBinding

    private val placeList = ArrayList<Place>()

    private var selectedDate: String? = null

    private lateinit var recommendationPlaceAdapter: RecommendationPlaceAdapter

    private lateinit var placeViewModel: PlaceViewModel

    @SuppressLint("NotifyDataSetChanged")
    val addPlaceLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == CustomizePlanActivity.RESULT_CODE && result.data != null) {
            @Suppress("DEPRECATION")
            val returnedPlace = result.data?.getParcelableExtra<Place>(EXTRA_RETURN_PLACE)
            Log.d("RecommendationPlanActivity", "Returned Place: $returnedPlace")
            returnedPlace?.let {
                if (!placeList.contains(it)) {
                    placeList.add(it)
                    binding.rvPlaces.adapter?.notifyItemInserted(placeList.size - 1)
                    recommendationPlaceAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupDate()
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

    private fun setupDate() {
        selectedDate = intent.getStringExtra(EXTRA_DATE)
        binding.planDate.text = selectedDate?.withDateFormat() ?: ""
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
        placeList.addAll(getListPlace())

        recommendationPlaceAdapter = RecommendationPlaceAdapter(placeViewModel)

        binding.rvPlaces.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@RecommendationPlanActivity)
            adapter = recommendationPlaceAdapter
        }

//        recommendationPlaceAdapter.setOnItemClickCallback(object : RecommendationPlaceAdapter.OnItemClickCallback {
//            override fun onItemClicked(items: Place) {
//                val dialogFragment = DetailPlaceFragment.newInstance(items)
//                dialogFragment.show(supportFragmentManager, "DetailPlaceFragment")
//            }
//        })

//        recommendationPlaceAdapter.setOnClearButtonClickCallback(object : RecommendationPlaceAdapter.OnClearButtonClickCallback {
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onClearButtonClicked(item: Place) {
//                placeList.remove(item)
//                recommendationPlaceAdapter.notifyDataSetChanged()
//            }
//        })
//
//        recommendationPlaceAdapter.setOnAddButtonClickCallback(object : RecommendationPlaceAdapter.OnAddButtonClickCallback {
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onAddButtonClicked(item: Place) {
//                val intent = Intent(this@RecommendationPlanActivity, CustomizePlanActivity::class.java).apply {
//                    putExtra(CustomizePlanActivity.EXTRA_PLACE, item)
//                    putExtra(CustomizePlanActivity.EXTRA_DATE, selectedDate)
//                }
//                addPlaceLauncher.launch(intent)
//
//                if (placeList.remove(item)) {
//                    recommendationPlaceAdapter.notifyDataSetChanged()
//                }
//            }
//        })
    }

//    private fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }

//    private fun showLoading(isLoading: Boolean) {
//        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
//    }
//
//    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
//        val factory = ViewModelFactory.getInstance(
//            activity.application,
//            UserPreference.getInstance(dataStore)
//        )
//        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
//    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        binding.rvPlaces.adapter?.notifyDataSetChanged()
    }

    companion object {
        const val EXTRA_DATE = "extra_date"
        const val EXTRA_RETURN_PLACE = "extra_return_place"
    }
}