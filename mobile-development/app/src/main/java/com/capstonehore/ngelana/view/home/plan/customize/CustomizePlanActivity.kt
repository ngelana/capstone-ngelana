package com.capstonehore.ngelana.view.home.plan.customize

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.PlanAdapter
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.databinding.ActivityCustomizePlanBinding
import com.capstonehore.ngelana.utils.withDateFormat
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.detail.DetailPlaceFragment
import com.capstonehore.ngelana.view.home.plan.PlanViewModel
import com.capstonehore.ngelana.view.home.plan.recommendation.RecommendationPlanActivity
import com.capstonehore.ngelana.view.home.plan.result.ResultPlanActivity

class CustomizePlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomizePlanBinding

    private var newPlace: PlaceItem? = null
    private var removedItem: PlaceItem? = null
    private var placeItem = mutableListOf<PlaceItem>()

    private var planName: String? = null
    private var selectedDate: String? = null

    private lateinit var planAdapter: PlanAdapter

    private lateinit var customizePlanViewModel: CustomizePlanViewModel
    private lateinit var planViewModel: PlanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomizePlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customizePlanViewModel = ViewModelProvider(this)[CustomizePlanViewModel::class.java]
        planViewModel = obtainViewModel(this@CustomizePlanActivity)

        placeItem = customizePlanViewModel.loadPlanList().toMutableList()

        @Suppress("DEPRECATION")
        newPlace = intent.getParcelableExtra(EXTRA_PLACE)

        setupAction()
        setupToolbar()
        setupDate()
        setupData()
        setupAdapter()
        updatePlanListAndSave()
    }

    private fun setupAction() {
        binding.submitButton.setOnClickListener {
            if (placeItem.isEmpty()) showToast(getString(R.string.empty_place)) else
                navigateToResult()
        }

        binding.ivAddPlace.setOnClickListener {
            val returnIntent = Intent().apply {
                putExtra(RecommendationPlanActivity.EXTRA_RETURN_PLACE, removedItem)
            }
            setResult(RESULT_CODE, returnIntent)
            finish()
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

    private fun setupData() {
        if (placeItem.isNotEmpty()) {
            binding.tvNoData.visibility = View.GONE
        } else {
            binding.tvNoData.visibility = View.VISIBLE
        }
    }

    private fun setupAdapter() {
        planAdapter = PlanAdapter()

        binding.rvPlaces.apply {
            layoutManager = LinearLayoutManager(this@CustomizePlanActivity)
            adapter = planAdapter
        }

        planAdapter.setOnItemClickCallback(object : PlanAdapter.OnItemClickCallback {
            override fun onItemClicked(item: PlaceItem?) {
                item?.let {
                    val dialogFragment = DetailPlaceFragment.newInstance(it)
                    dialogFragment.show(supportFragmentManager, "DetailPlaceFragment")
                }
            }
        })

        planAdapter.setOnClearButtonClickCallback(object : PlanAdapter.OnClearButtonClickCallback {
            @SuppressLint("NotifyDataSetChanged")
            override fun onClearButtonClicked(item: PlaceItem?) {
                item?.let {
                    removedItem = it

                    placeItem.remove(it)
                    planAdapter.notifyDataSetChanged()
                    customizePlanViewModel.savePlanList(placeItem)
                    setupData()

                    val returnIntent = Intent().apply {
                        putExtra(RecommendationPlanActivity.EXTRA_RETURN_PLACE, it)
                    }
                    Log.d("CustomizePlanActivity", "Returning place: $it")
                    setResult(RESULT_CODE, returnIntent)
                }
            }
        })

        planAdapter.submitList(placeItem)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updatePlanListAndSave() {
        newPlace?.let {
            if (!placeItem.contains(it)) {
                placeItem.add(it)
                customizePlanViewModel.savePlanList(placeItem)
                setupData()
            }
        }
    }

    private fun navigateToResult() {
        val planName = binding.edPlanName.text?.toString()
        startActivity(Intent(this@CustomizePlanActivity, ResultPlanActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): PlanViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[PlanViewModel::class.java]
    }

    companion object {
        const val EXTRA_PLACE = "extra_place"
        const val EXTRA_DATE = "extra_date"
        const val RESULT_CODE = 110
    }
}