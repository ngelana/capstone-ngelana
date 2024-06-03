package com.capstonehore.ngelana.view.home.plan.customize

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.adapter.PlanAdapter
import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.databinding.ActivityCustomizePlanBinding
import com.capstonehore.ngelana.view.detail.DetailPlaceFragment
import com.capstonehore.ngelana.view.home.plan.result.ResultPlanActivity

class CustomizePlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomizePlanBinding

    private var list = ArrayList<Place>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomizePlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvPlaces.setHasFixedSize(true)

        @Suppress("DEPRECATION")
        list = intent.getParcelableArrayListExtra(EXTRA_PLACE) ?: ArrayList()
        showRecyclerList()

        binding.submitButton.setOnClickListener {
            val resultIntent = Intent(this@CustomizePlanActivity, ResultPlanActivity::class.java)
            resultIntent.putParcelableArrayListExtra(ResultPlanActivity.EXTRA_RESULT_PLACE, list)
            startActivity(resultIntent)
        }

        binding.addPlaceCard.setOnClickListener{
            val resultIntent = Intent()
            resultIntent.putParcelableArrayListExtra(EXTRA_PLACE, list)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun showRecyclerList() {
        binding.rvPlaces.layoutManager = LinearLayoutManager(this)
        val planAdapter = PlanAdapter(list)
        binding.rvPlaces.adapter = planAdapter

        planAdapter.setOnItemClickCallback(object : PlanAdapter.OnItemClickCallback {
            override fun onItemClicked(items: Place) {
                val dialogFragment = DetailPlaceFragment.newInstance(items)
                dialogFragment.show(supportFragmentManager, "DetailPlaceFragment")
            }
        })
    }

    companion object {
        const val EXTRA_PLACE = "extra_place"
    }
}