package com.capstonehore.ngelana.view.explore.place.tourist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.PlaceAdapter
import com.capstonehore.ngelana.data.Place
import com.capstonehore.ngelana.databinding.ActivityTouristAttractionsBinding
import com.capstonehore.ngelana.view.detail.DetailPlaceFragment
import com.capstonehore.ngelana.view.explore.ExploreFragment

class TouristAttractionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTouristAttractionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTouristAttractionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupView()
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

    private fun getListPlace(): ArrayList<Place> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataImage = resources.getStringArray(R.array.data_image)
        val listPlace= ArrayList<Place>()

        for (i in dataName.indices) {
            val place = Place(dataName[i], dataDescription[i], dataImage[i])
            listPlace.add(place)
        }
        return listPlace
    }

    private fun setupView() {
        val placeList = getListPlace()
        val placeAdapter = PlaceAdapter(placeList)

        binding.rvPlaces.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@TouristAttractionsActivity)
            adapter = placeAdapter
        }

        placeAdapter.setOnItemClickCallback(object : PlaceAdapter.OnItemClickCallback {
            override fun onItemClicked(items: Place) {
                val dialogFragment = DetailPlaceFragment.newInstance(items)
                dialogFragment.show(supportFragmentManager, "DetailPlaceFragment")
            }
        })
    }

}