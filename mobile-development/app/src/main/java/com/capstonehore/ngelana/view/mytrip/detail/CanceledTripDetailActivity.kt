package com.capstonehore.ngelana.view.mytrip.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.remote.response.PlanUserItem
import com.capstonehore.ngelana.databinding.ActivityCanceledTripDetailBinding
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.home.plan.PlanViewModel
import com.capstonehore.ngelana.view.main.MainActivity

class CanceledTripDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCanceledTripDetailBinding

    private var planUserItem: PlanUserItem? = null

    private lateinit var planViewModel: PlanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCanceledTripDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        planViewModel = obtainViewModel(this@CanceledTripDetailActivity)

        setupAction()
        setupToolbar()
        setupPlanData()
    }

    private fun setupAction() {
        binding.startPlanningButton.setOnClickListener {
            startActivity(Intent(this@CanceledTripDetailActivity, MainActivity::class.java))
        }
    }

    private fun setupToolbar() {
        with(binding) {
            setSupportActionBar(topAppBar)
            topAppBar.title = binding.planName.text
            topAppBar.setNavigationIcon(R.drawable.ic_arrow_back)
            topAppBar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun setupPlanData() {
        @Suppress("DEPRECATION")
        planUserItem = intent.getParcelableExtra(EXTRA_PLAN_ITEM)
        planUserItem?.let {
            binding.planName.text = it.name
            binding.planDate.text = it.date
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): PlanViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[PlanViewModel::class.java]
    }

    companion object {
        private const val TAG = "CanceledTripDetailActivity"
        const val EXTRA_PLAN_ITEM = "extra_plan_item"
    }
}