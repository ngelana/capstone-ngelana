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

class ResultPlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultPlanBinding

    private lateinit var list: ArrayList<Place>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        list = intent.getParcelableArrayListExtra(EXTRA_RESULT_PLACE) ?: ArrayList()

        val planAdapter = PlanAdapter(list)
        binding.rvPlaces.layoutManager = LinearLayoutManager(this)
        binding.rvPlaces.adapter = planAdapter

        planAdapter.setOnItemClickCallback(object : PlanAdapter.OnItemClickCallback {
            override fun onItemClicked(items: Place) {
                val dialogFragment = DetailPlaceFragment.newInstance(items)
                dialogFragment.show(supportFragmentManager, "DetailPlaceFragment")
            }
        })

        binding.backToHomeButton.setOnClickListener {
            startActivity(Intent(this@ResultPlanActivity, MainActivity::class.java))
            finish()
        }
    }

    companion object {
        const val EXTRA_RESULT_PLACE = "extra_result_place"
    }
}