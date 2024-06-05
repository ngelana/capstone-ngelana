package com.capstonehore.ngelana.view.home.plan.customize

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.PlanAdapter
import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.databinding.ActivityCustomizePlanBinding
import com.capstonehore.ngelana.view.detail.DetailPlaceFragment
import com.capstonehore.ngelana.view.home.plan.result.ResultPlanActivity

class CustomizePlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomizePlanBinding

    private var planList = ArrayList<Place>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomizePlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        planList = intent.getParcelableArrayListExtra(EXTRA_PLACE) ?: ArrayList()
        setupData(planList)
    }

    private fun setupAction() {
        binding.submitButton.setOnClickListener {
            val resultIntent = Intent(this@CustomizePlanActivity, ResultPlanActivity::class.java)
            resultIntent.putParcelableArrayListExtra(ResultPlanActivity.EXTRA_RESULT_PLACE, planList)
            startActivity(resultIntent)
        }

        binding.addPlaceCard.setOnClickListener{
            val resultIntent = Intent()
            resultIntent.putParcelableArrayListExtra(EXTRA_PLACE, planList)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun setupStatusBar() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.dark_blue)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            }
        }
    }

    private fun setupData(data: ArrayList<Place>) {
        if (data.isNotEmpty()) {
            setupAction()
            setupStatusBar()
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

    companion object {
        const val EXTRA_PLACE = "extra_place"
    }
}