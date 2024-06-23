package com.capstonehore.ngelana.view.home.plan.customize

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.databinding.ActivityCustomizePlanBinding
import com.capstonehore.ngelana.utils.withDateFormat
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.home.plan.PlanViewModel
import com.capstonehore.ngelana.view.home.plan.recommendation.RecommendationPlanActivity
import com.capstonehore.ngelana.view.home.plan.result.ResultPlanActivity

class CustomizePlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomizePlanBinding

    private lateinit var customizePlanViewModel: CustomizePlanViewModel

    private var planList = ArrayList<Place>()
    private var newPlace: Place? = null
    private var planName: String? = null
    private var selectedDate: String? = null

    private lateinit var planViewModel: PlanViewModel

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SESSION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomizePlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customizePlanViewModel = CustomizePlanViewModel(this)

        planList = customizePlanViewModel.loadPlanList()

        @Suppress("DEPRECATION")
        newPlace = intent.getParcelableExtra(EXTRA_PLACE)

        setupAction()
        setupToolbar()
        setupDate()
        setupData()
//        setupView()
        updatePlanListAndSave()
    }

    private fun setupAction() {
        binding.submitButton.setOnClickListener {
            if (planList.isEmpty()) {
                showToast(getString(R.string.empty_place))
            } else {
                planName = binding.edPlanName.text?.toString()
                navigateToResultActivity()
            }
        }

        binding.ivAddPlace.setOnClickListener{
            val addPlaceIntent = Intent().apply {
                putExtra(RecommendationPlanActivity.EXTRA_RETURN_PLACE, planList)
            }
            setResult(RESULT_CODE, addPlaceIntent)
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
        if (planList.isNotEmpty()) {
            binding.tvNoData.visibility = View.GONE
        } else {
            binding.tvNoData.visibility = View.VISIBLE
        }
    }

//    private fun setupView() {
//        val planAdapter = PlanAdapter(planList)
//
//        binding.rvPlaces.apply {
//            setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(this@CustomizePlanActivity)
//            adapter = planAdapter
//        }

//        planAdapter.setOnItemClickCallback(object :
//            PlanAdapter.OnItemClickCallback {
//            override fun onItemClicked(items: Place) {
//                val dialogFragment = DetailPlaceFragment.newInstance(items)
//                dialogFragment.show(supportFragmentManager, "DetailPlaceFragment")
//            }
//        })

//        planAdapter.setOnClearButtonClickCallback(object : PlanAdapter.OnClearButtonClickCallback {
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onClearButtonClicked(item: Place) {
//                planList.remove(item)
//                planAdapter.notifyDataSetChanged()
//                customizePlanViewModel.savePlanList(planList)
//                setupData()
//
//                val returnIntent = Intent().apply {
//                    putExtra(RecommendationPlanActivity.EXTRA_RETURN_PLACE, item)
//                }
//                Log.d("CustomizePlanActivity", "Returning place: $item")
//                setResult(RESULT_CODE, returnIntent)
//            }
//        })
//    }

    private fun updatePlanListAndSave() {
        newPlace?.let {
            if (!planList.contains(it)) {
                planList.add(it)
                customizePlanViewModel.savePlanList(planList)
                setupData()
            }
        }
    }

    private fun navigateToResultActivity() {
        val planName = binding.edPlanName.text?.toString()
        val intent = Intent(this@CustomizePlanActivity, ResultPlanActivity::class.java).apply {
            putParcelableArrayListExtra(ResultPlanActivity.EXTRA_RESULT_PLACE, planList)
            putExtra(ResultPlanActivity.EXTRA_NAME, planName)
            putExtra(ResultPlanActivity.EXTRA_DATE, selectedDate)
        }
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): PlanViewModel {
        val factory = ViewModelFactory.getInstance(
            activity.application,
            UserPreferences.getInstance(dataStore)
        )
        return ViewModelProvider(activity, factory)[PlanViewModel::class.java]
    }

    companion object {
        const val EXTRA_PLACE = "extra_place"
        const val EXTRA_DATE = "extra_date"
        const val RESULT_CODE = 110
        const val SESSION = "session"
    }
}