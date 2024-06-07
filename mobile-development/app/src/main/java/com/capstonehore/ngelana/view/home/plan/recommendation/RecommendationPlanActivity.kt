package com.capstonehore.ngelana.view.home.plan.recommendation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.RecommendationPlaceAdapter
import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.databinding.ActivityRecommendationPlanBinding
import com.capstonehore.ngelana.view.detail.DetailPlaceFragment
import com.capstonehore.ngelana.view.home.plan.customize.CustomizePlanActivity

class RecommendationPlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendationPlanBinding

    private val placeList = ArrayList<Place>()

    private val ADD_PLACE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupView()
    }

    private fun setupAction() {
        binding.submitButton.setOnClickListener {
            val intent = Intent(this@RecommendationPlanActivity, CustomizePlanActivity::class.java).apply {
                putParcelableArrayListExtra(CustomizePlanActivity.EXTRA_PLACE, placeList)
            }
            @Suppress("DEPRECATION")
            startActivityForResult(intent, ADD_PLACE_REQUEST)
        }
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
        val recommendationPlaceAdapter = RecommendationPlaceAdapter(placeList)

        binding.rvPlaces.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@RecommendationPlanActivity)
            adapter = recommendationPlaceAdapter
        }

        recommendationPlaceAdapter.setOnItemClickCallback(object : RecommendationPlaceAdapter.OnItemClickCallback {
            override fun onItemClicked(items: Place) {
                val dialogFragment = DetailPlaceFragment.newInstance(items)
                dialogFragment.show(supportFragmentManager, "DetailPlaceFragment")
            }
        })

        recommendationPlaceAdapter.setOnClearButtonClickCallback(object : RecommendationPlaceAdapter.OnClearButtonClickCallback {
            @SuppressLint("NotifyDataSetChanged")
            override fun onClearButtonClicked(item: Place) {
                placeList.remove(item)
                recommendationPlaceAdapter.notifyDataSetChanged()
            }
        })

        recommendationPlaceAdapter.setOnAddButtonClickCallback(object : RecommendationPlaceAdapter.OnAddButtonClickCallback {
            override fun onAddButtonClicked(item: Place) {
                val intent = Intent(this@RecommendationPlanActivity, CustomizePlanActivity::class.java).apply {
                    putExtra(CustomizePlanActivity.EXTRA_PLACE, item)
                }
                @Suppress("DEPRECATION")
                startActivityForResult(intent, ADD_PLACE_REQUEST)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_PLACE_REQUEST && resultCode == Activity.RESULT_OK) {
            @Suppress("DEPRECATION")
            val updatedList = data?.getParcelableArrayListExtra<Place>(CustomizePlanActivity.EXTRA_PLACE)
            updatedList?.let {
                placeList.clear()
                placeList.addAll(it)
                binding.rvPlaces.adapter?.notifyDataSetChanged()
            }
        }
    }
}