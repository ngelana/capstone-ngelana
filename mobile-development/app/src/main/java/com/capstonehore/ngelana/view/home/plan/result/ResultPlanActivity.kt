package com.capstonehore.ngelana.view.home.plan.result

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.databinding.ActivityResultPlanBinding
import com.capstonehore.ngelana.databinding.CustomAlertDialogBinding
import com.capstonehore.ngelana.utils.withDateFormat
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.home.plan.PlanViewModel
import com.capstonehore.ngelana.view.main.MainActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultPlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultPlanBinding

    private lateinit var planList: ArrayList<Place>

    private var planName: String? = null
    private var selectedDate: String? = null

    private lateinit var planViewModel: PlanViewModel

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SESSION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        planViewModel = obtainViewModel(this@ResultPlanActivity)

        @Suppress("DEPRECATION")
        planList = intent.getParcelableArrayListExtra(EXTRA_RESULT_PLACE) ?: ArrayList()
        setupAction()
//        setupData(planList)
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
            binding.planDate.text = getString(R.string.not_available)
        }
    }

//    private fun setupData(data: ArrayList<Place>) {
//        if (data.isNotEmpty()) {
//            setupView()
//        }
//    }

//    private fun setupView() {
//        val planResultAdapter = PlanResultAdapter(planList)
//
//        binding.rvPlaces.apply {
//            layoutManager = LinearLayoutManager(this@ResultPlanActivity)
//            adapter = planResultAdapter
//        }

//        planResultAdapter.setOnItemClickCallback(object :
//            PlanResultAdapter.OnItemClickCallback {
//            override fun onItemClicked(items: Place) {
//                val dialogFragment = DetailPlaceFragment.newInstance(items)
//                dialogFragment.show(supportFragmentManager, "DetailPlaceFragment")
//            }
//        })
//    }

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
                    setBackgroundColor(ContextCompat.getColor(this@ResultPlanActivity, R.color.light_grey))
                    setTextColor(ContextCompat.getColor(this@ResultPlanActivity, R.color.black))
                    setOnClickListener {
                        moveToMain()
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

    private fun moveToMain() {
        startActivity(Intent(this@ResultPlanActivity, MainActivity::class.java))
        finish()
    }

    private fun obtainViewModel(activity: AppCompatActivity): PlanViewModel {
        val factory = ViewModelFactory.getInstance(
            activity.application,
            UserPreferences.getInstance(dataStore)
        )
        return ViewModelProvider(activity, factory)[PlanViewModel::class.java]
    }

    companion object {
        const val EXTRA_RESULT_PLACE = "extra_result_place"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DATE = "extra_date"
        const val SESSION = "session"
    }
}