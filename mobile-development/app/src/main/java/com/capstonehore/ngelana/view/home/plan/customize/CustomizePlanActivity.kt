package com.capstonehore.ngelana.view.home.plan.customize

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.adapter.PlanAdapter
import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.databinding.ActivityCustomizePlanBinding
import com.capstonehore.ngelana.view.detail.DetailPlaceFragment
import com.capstonehore.ngelana.view.home.plan.result.ResultPlanActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CustomizePlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomizePlanBinding

    private var planList = ArrayList<Place>()

    private var selectedDate: LocalDate? = null
    private val dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy")

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
            val intent = Intent(this@CustomizePlanActivity, ResultPlanActivity::class.java).apply {
                putParcelableArrayListExtra(ResultPlanActivity.EXTRA_RESULT_PLACE, planList)
                putExtra(ResultPlanActivity.EXTRA_DATE, selectedDate?.format(dateFormat))
            }
            startActivity(intent)
        }

        binding.addPlaceCard.setOnClickListener{
            val resultIntent = Intent()
            resultIntent.putParcelableArrayListExtra(EXTRA_PLACE, planList)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun setupDate() {
        val receivedDateStr = intent.getStringExtra(EXTRA_DATE)
        if (receivedDateStr != null) {
            selectedDate = LocalDate.parse(receivedDateStr, dateFormat)
            binding.planDate.text = selectedDate?.format(dateFormat)
        } else {
            binding.planDate.text = ""
        }
    }

    private fun setupData(data: ArrayList<Place>) {
        if (data.isNotEmpty()) {
            setupAction()
            setupDate()
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
        const val EXTRA_DATE = "extra_date"
    }
}