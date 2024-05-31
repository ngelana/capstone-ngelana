package com.capstonehore.ngelana.view.plan.result

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.adapter.PlanAdapter
import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.databinding.ActivityResultPlanBinding

class ResultPlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultPlanBinding

    private lateinit var list: ArrayList<Place>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        list = intent.getParcelableArrayListExtra(EXTRA_RESULT_PLACE) ?: ArrayList()

        val adapter = PlanAdapter(list)
        binding.rvPlaces.layoutManager = LinearLayoutManager(this)
        binding.rvPlaces.adapter = adapter
    }

    companion object {
        const val EXTRA_RESULT_PLACE = "extra_result_place"
    }

}