package com.capstonehore.ngelana.view.home.plan.result

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.adapter.PlanResultAdapter
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.databinding.ActivityResultPlanBinding
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.detail.DetailPlaceFragment
import com.capstonehore.ngelana.view.home.plan.PlanViewModel
import com.capstonehore.ngelana.view.main.MainActivity

class ResultPlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultPlanBinding

    private lateinit var planResultAdapter: PlanResultAdapter

    private lateinit var planViewModel: PlanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        planViewModel = obtainViewModel(this@ResultPlanActivity)

        setupAction()
        setupAdapter()
        setupPlanData()
        setupView()
    }

    private fun setupAction() {
        binding.backToHomeButton.setOnClickListener {
            moveToMain()
        }
    }

    private fun setupAdapter() {
        planResultAdapter = PlanResultAdapter()

        binding.rvPlaces.apply {
            layoutManager = LinearLayoutManager(this@ResultPlanActivity)
            adapter = planResultAdapter
        }

        planResultAdapter.setOnItemClickCallback(object : PlanResultAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PlaceItem?) {
                data?.let {
                    val dialogFragment = DetailPlaceFragment.newInstance(data)
                    dialogFragment.show(supportFragmentManager, "DetailPlaceFragment")
                }
            }
        })
    }

    private fun setupPlanData() {
        planViewModel.getPlanByUserId().observe(this) { result ->
            if (result is Result.Success) {
                val response = result.data
                response.map {
                    binding.planName.text = it.name
                    binding.planDate.text = it.date
                }
            }
        }
    }

    private fun setupView() {
        planViewModel.getPlanDetailByUserId().observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    showLoading(false)

                    val response = result.data
                    response.let { item ->
                        planResultAdapter.submitList(item)
                    }
                    Log.d(TAG, "Successfully Show Upcoming Trip Plan: $response")
                }
                is Result.Error -> {
                    showLoading(false)

                    showToast(result.error)
                    Log.d(TAG, "Failed to Show Upcoming Trip Plan: ${result.error}")
                }
                is Result.Loading -> showLoading(true)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading:Boolean){
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun moveToMain() {
        startActivity(Intent(this@ResultPlanActivity, MainActivity::class.java))
        finish()
    }

    private fun obtainViewModel(activity: AppCompatActivity): PlanViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[PlanViewModel::class.java]
    }

    companion object {
        private const val TAG = "ResultPlanActivity"
    }
}