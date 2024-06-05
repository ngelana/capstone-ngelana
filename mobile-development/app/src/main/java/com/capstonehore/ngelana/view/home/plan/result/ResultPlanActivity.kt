package com.capstonehore.ngelana.view.home.plan.result

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.PlanAdapter
import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.databinding.ActivityResultPlanBinding
import com.capstonehore.ngelana.view.detail.DetailPlaceFragment
import com.capstonehore.ngelana.view.main.MainActivity

class ResultPlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultPlanBinding

    private lateinit var planList: ArrayList<Place>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        planList = intent.getParcelableArrayListExtra(EXTRA_RESULT_PLACE) ?: ArrayList()
        setupData(planList)

    }

    private fun setupAction() {
        binding.backToHomeButton.setOnClickListener {
            startActivity(Intent(this@ResultPlanActivity, MainActivity::class.java))
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
    }
}