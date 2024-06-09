package com.capstonehore.ngelana.view.home.plan.result

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.adapter.PlanAdapter
import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.databinding.ActivityResultPlanBinding
import com.capstonehore.ngelana.view.detail.DetailPlaceFragment
import com.capstonehore.ngelana.view.main.MainActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ResultPlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultPlanBinding

    private lateinit var planList: ArrayList<Place>

    private var selectedDate: LocalDate? = null
    private val dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        planList = intent.getParcelableArrayListExtra(EXTRA_RESULT_PLACE) ?: ArrayList()
        setupData(planList)
        setupDate()

    }

    private fun setupAction() {
        binding.backToHomeButton.setOnClickListener {
            startActivity(Intent(this@ResultPlanActivity, MainActivity::class.java))
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
            setupView()
        }
    }

    private fun setupView() {
        val planAdapter = PlanAdapter(planList)

        binding.rvPlaces.apply {
            layoutManager = LinearLayoutManager(this@ResultPlanActivity)
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
        const val EXTRA_RESULT_PLACE = "extra_result_place"
        const val EXTRA_DATE = "extra_date"
    }
}