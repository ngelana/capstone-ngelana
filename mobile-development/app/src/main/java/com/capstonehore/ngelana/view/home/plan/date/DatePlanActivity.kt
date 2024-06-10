package com.capstonehore.ngelana.view.home.plan.date

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.databinding.ActivityDatePlanBinding
import com.capstonehore.ngelana.view.home.HomeFragment
import com.capstonehore.ngelana.view.home.plan.recommendation.RecommendationPlanActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DatePlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDatePlanBinding

    private var selectedDate: Date? = null
    private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatePlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupAnimation()
    }

    private fun setupAction() {
        binding.backButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main, HomeFragment())
                .addToBackStack(null)
                .commit()
        }
        binding.nextButton.setOnClickListener {
            val dateStr = selectedDate?.let { dateFormat.format(it) } ?: ""

            if (dateStr.isEmpty()) {
                showToast(getString(R.string.empty_date))
            } else {
                startActivity(Intent(this@DatePlanActivity, RecommendationPlanActivity::class.java)
                    .putExtra(RecommendationPlanActivity.EXTRA_DATE, dateStr))
            }
        }
        binding.edDate.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePickerDialog()
            }
        }
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.logoImage, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val tvTitle = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(300)
        val tvDate = ObjectAnimator.ofFloat(binding.tvDate, View.ALPHA, 1f).setDuration(300)
        val nextButton =
            ObjectAnimator.ofFloat(binding.nextButton, View.ALPHA, 1f).setDuration(500)
        val backButton = ObjectAnimator.ofFloat(binding.backButton, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(nextButton, backButton)
        }

        AnimatorSet().apply {
            playSequentially(
                tvTitle,
                tvDate,
                together
            )
            start()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }
                selectedDate = selectedCalendar.time
                binding.edDate.setText(dateFormat.format(selectedCalendar.time))
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}