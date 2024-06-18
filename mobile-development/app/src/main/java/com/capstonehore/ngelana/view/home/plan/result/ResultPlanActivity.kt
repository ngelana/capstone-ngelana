package com.capstonehore.ngelana.view.home.plan.result

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.adapter.PlanResultAdapter
import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.databinding.ActivityResultPlanBinding
import com.capstonehore.ngelana.utils.withDateFormat
import com.capstonehore.ngelana.view.detail.DetailPlaceFragment
import com.capstonehore.ngelana.view.main.MainActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultPlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultPlanBinding

    private lateinit var planList: ArrayList<Place>

    private var planName: String? = null
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        planList = intent.getParcelableArrayListExtra(EXTRA_RESULT_PLACE) ?: ArrayList()
        setupAction()
        setupData(planList)
        setupName()
        setupDate()
    }

    private fun setupAction() {
        binding.backToHomeButton.setOnClickListener {
            startActivity(Intent(this@ResultPlanActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun setupName() {
        planName = intent.getStringExtra(EXTRA_NAME)
        if (planName != null && planName!!.isNotEmpty()) {
            binding.planName.text = planName
        } else {
            binding.planName.text = generateNewName()
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
        }
    }

    private fun setupView() {
        val planResultAdapter = PlanResultAdapter(planList)

        binding.rvPlaces.apply {
            layoutManager = LinearLayoutManager(this@ResultPlanActivity)
            adapter = planResultAdapter
        }

        planResultAdapter.setOnItemClickCallback(object :
            PlanResultAdapter.OnItemClickCallback {
            override fun onItemClicked(items: Place) {
                val dialogFragment = DetailPlaceFragment.newInstance(items)
                dialogFragment.show(supportFragmentManager, "DetailPlaceFragment")
            }
        })
    }

    private fun generateNewName(): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        return "Ngelana_Plan_Trip-$currentDate"
    }

    companion object {
        const val EXTRA_RESULT_PLACE = "extra_result_place"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DATE = "extra_date"
    }
}