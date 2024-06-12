package com.capstonehore.ngelana.view.home.plan.customize

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.PlanAdapter
import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.databinding.ActivityCustomizePlanBinding
import com.capstonehore.ngelana.utils.withDateFormat
import com.capstonehore.ngelana.view.detail.DetailPlaceFragment
import com.capstonehore.ngelana.view.home.plan.result.ResultPlanActivity

class CustomizePlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomizePlanBinding

    private var planList = ArrayList<Place>()

    private var planName: String? = null
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomizePlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        planList = intent.getParcelableArrayListExtra(EXTRA_PLACE) ?: ArrayList()
        setupAction()
        setupToolbar()
        setupData(planList)
        setupDate()
    }

    private fun setupAction() {
        binding.submitButton.setOnClickListener {
            planName = binding.edPlanName.text?.toString()
            navigateToResultActivity()
        }

        binding.addPlaceCard.setOnClickListener{
            val resultIntent = Intent()
            resultIntent.putParcelableArrayListExtra(EXTRA_PLACE, planList)
            setResult(Activity.RESULT_OK, resultIntent)
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
        if (selectedDate != null) {
            binding.planDate.text = selectedDate?.withDateFormat()
        } else {
            binding.planDate.text = ""
        }
    }

    private fun setupData(data: ArrayList<Place>) {
        if (data.isNotEmpty()) {
            setupView()
        } else {
            binding.tvNoData.visibility = View.VISIBLE

            binding.rvPlaces.visibility = View.GONE
            binding.addPlaceCard.visibility = View.GONE
            binding.submitButton.visibility = View.GONE
        }
    }

    private fun setupView() {
        val planAdapter = PlanAdapter(planList)

        binding.rvPlaces.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@CustomizePlanActivity)
            adapter = planAdapter
        }

        planAdapter.setOnItemClickCallback(object :
            PlanAdapter.OnItemClickCallback {
            override fun onItemClicked(items: Place) {
                val dialogFragment = DetailPlaceFragment.newInstance(items)
                dialogFragment.show(supportFragmentManager, "DetailPlaceFragment")
            }
        })
    }

    private fun navigateToResultActivity() {
        val intent = Intent(this@CustomizePlanActivity, ResultPlanActivity::class.java).apply {
            putParcelableArrayListExtra(ResultPlanActivity.EXTRA_RESULT_PLACE, planList)
            putExtra(ResultPlanActivity.EXTRA_NAME, planName)
            putExtra(ResultPlanActivity.EXTRA_DATE, selectedDate)
        }
        startActivity(intent)
        finish()
    }

    companion object {
        const val EXTRA_PLACE = "extra_place"
        const val EXTRA_DATE = "extra_date"
    }
}