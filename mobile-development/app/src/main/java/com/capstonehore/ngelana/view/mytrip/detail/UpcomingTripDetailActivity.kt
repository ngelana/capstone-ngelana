package com.capstonehore.ngelana.view.mytrip.detail

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.remote.response.PlanUserItem
import com.capstonehore.ngelana.databinding.ActivityUpcomingTripDetailBinding
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.home.plan.PlanViewModel

class UpcomingTripDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpcomingTripDetailBinding

    private var planUserItem: PlanUserItem? = null

    private lateinit var planViewModel: PlanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpcomingTripDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        planViewModel = obtainViewModel(this@UpcomingTripDetailActivity)

        setupAction()
        setupPlanData()
        setupToolbar()
    }

    private fun setupAction() {
        binding.canceledTripButton.setOnClickListener {
            setupCancel()
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

    private fun setupCancel() {
        AlertDialog.Builder(this).apply {
            val message = getString(R.string.cancel_trip_confirmation_message)

            setTitle(getString(R.string.cancel_the_trip))
            setMessage(message)

            setPositiveButton(getString(R.string.canceled_trip)) { _, _ ->
                planUserItem?.let {
                    planViewModel.addCanceledPlan(it)
                }
            }

            setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }

            create().show()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): PlanViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[PlanViewModel::class.java]
    }

    companion object {
        private const val TAG = "UpcomingTripDetailActivity"
        const val EXTRA_PLAN_ITEM = "extra_plan_item"
    }
}