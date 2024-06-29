package com.capstonehore.ngelana.view.home.plan.customize

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.PlanAdapter
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.remote.response.DataPlacesItem
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.databinding.ActivityCustomizePlanBinding
import com.capstonehore.ngelana.databinding.CustomAlertDialogBinding
import com.capstonehore.ngelana.utils.dateFormat
import com.capstonehore.ngelana.utils.withDateFormat
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.detail.DetailPlaceFragment
import com.capstonehore.ngelana.view.home.plan.PlanViewModel
import com.capstonehore.ngelana.view.home.plan.recommendation.RecommendationPlanActivity
import com.capstonehore.ngelana.view.home.plan.result.ResultPlanActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CustomizePlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomizePlanBinding

    private var newPlace: PlaceItem? = null
    private var removedItem = mutableListOf<PlaceItem>()
    private var placeItem = mutableListOf<PlaceItem>()

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
        selectedDate = intent.getStringExtra(EXTRA_DATE)

        setupAction()
        setupToolbar()
        setupDate()
        setupData()
        setupAdapter()
        updatePlanListAndSave()
    }

    private fun setupAction() {
        binding.submitButton.setOnClickListener {
            if (placeItem.isEmpty()) {
                showToast(getString(R.string.empty_place))
            } else {
                setupPlan()
            }
        }

        binding.ivAddPlace.setOnClickListener {
            val returnIntent = Intent().apply {
                putParcelableArrayListExtra(
                    RecommendationPlanActivity.EXTRA_RETURN_PLACE,
                    ArrayList(removedItem)
                )
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
                    removedItem.add(it)

                    placeItem.remove(it)
                    planAdapter.notifyDataSetChanged()
                    customizePlanViewModel.savePlanList(placeItem)
                    setupData()

                    val returnIntent = Intent().apply {
                        putParcelableArrayListExtra(
                            RecommendationPlanActivity.EXTRA_RETURN_PLACE,
                            ArrayList(removedItem)
                        )
                    }
                    Log.d("CustomizePlanActivity", "Returning place: $it")
                    setResult(RESULT_CODE, returnIntent)
                }
            }
        })

        planAdapter.submitList(placeItem)
    }

    private fun setupPlan() {
        val edPlanName = binding.edPlanName.text.toString()

        val planName = if (edPlanName.isNotEmpty()) binding.edPlanName.text.toString()
        else generateNewName()

        val planDate = selectedDate?.dateFormat() ?: getString(R.string.not_available)

        val dataPlacesItemList = placeItem.map { DataPlacesItem(it) }

        planViewModel.setPlanResult(planName, planDate, dataPlacesItemList)
            .observe(this) { result ->
                when (result) {
                    is Result.Success -> {
                        showLoading(false)

                        val response = result.data
                        showCustomAlertDialog(true, "")
                        Log.d(TAG, "Successfully created plan: $response")
                    }
                    is Result.Error -> {
                        showLoading(false)

                        showCustomAlertDialog(false, result.error)
                        Log.d(TAG, "Failed to create plan: ${result.error}")
                    }
                    is Result.Loading -> showLoading(true)
                }
            }
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

    private fun generateNewName(): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        return "Ngelana_Plan_Trip-$currentDate"
    }

    private fun showCustomAlertDialog(isSuccess: Boolean, message: String) {
        val inflater = LayoutInflater.from(this)
        val alertLayout = CustomAlertDialogBinding.inflate(inflater)

        val builder = AlertDialog.Builder(this)
        builder.setView(alertLayout.root)

        val dialog = builder.create()
        dialog.show()

        if (isSuccess) {
            with(alertLayout) {
                alertIcon.setImageResource(R.drawable.ic_check_circle)
                alertTitle.text = getString(R.string.success_completed_title)
                alertMessage.text = getString(R.string.plan_creation_success_message)

                submitButton.setOnClickListener {
                    moveToResult()
                    dialog.dismiss()
                }
            }
        } else {
            with(alertLayout) {
                alertIcon.setImageResource(R.drawable.ic_error)
                alertTitle.text = getString(R.string.plan_creation_failed)
                alertMessage.text = message

                submitButton.apply {
                    text = getString(R.string.cancel)
                    setBackgroundColor(
                        ContextCompat.getColor(
                            this@CustomizePlanActivity,
                            R.color.light_grey
                        )
                    )
                    setTextColor(ContextCompat.getColor(this@CustomizePlanActivity, R.color.black))
                    setOnClickListener {
                        dialog.dismiss()
                    }
                }
            }
        }

        // Animation
        val scaleX = ObjectAnimator.ofFloat(alertLayout.alertIcon, "scaleX", 0.5f, 1f)
        val scaleY = ObjectAnimator.ofFloat(alertLayout.alertIcon, "scaleY", 0.5f, 1f)
        val tvTitle = ObjectAnimator.ofFloat(alertLayout.alertTitle, View.ALPHA, 0f, 1f)
        val tvMessage = ObjectAnimator.ofFloat(alertLayout.alertMessage, View.ALPHA, 0f, 1f)
        val submitButton = ObjectAnimator.ofFloat(alertLayout.submitButton, View.ALPHA, 0f, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, tvTitle, tvMessage, submitButton)
        animatorSet.duration = 800
        animatorSet.start()
    }

    private fun moveToResult() {
        startActivity(Intent(this@CustomizePlanActivity, ResultPlanActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): PlanViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[PlanViewModel::class.java]
    }

    companion object {
        private const val TAG = "CustomizePlanActivity"
        const val EXTRA_PLACE = "extra_place"
        const val EXTRA_DATE = "extra_date"
        const val RESULT_CODE = 110
    }
}