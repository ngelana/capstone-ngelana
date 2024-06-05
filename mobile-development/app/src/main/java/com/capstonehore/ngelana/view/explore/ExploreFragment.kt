package com.capstonehore.ngelana.view.explore

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.CategoryAdapter
import com.capstonehore.ngelana.data.local.entity.Category
import com.capstonehore.ngelana.databinding.FragmentExploreBinding
import com.capstonehore.ngelana.view.explore.place.culinary.CulinarySpotActivity
import com.capstonehore.ngelana.view.explore.place.lodging.LodgingActivity
import com.capstonehore.ngelana.view.explore.place.tourist.TouristAttractionsActivity
import com.capstonehore.ngelana.view.home.HomeViewModel

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null

    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }


    private fun getListCategory(): ArrayList<Category> {
        val dataName = resources.getStringArray(R.array.data_category)
        val dataImage = resources.getStringArray(R.array.data_image_category)
        val listPlace= ArrayList<Category>()

        for (i in dataName.indices) {
            val place = Category(dataName[i], dataImage[i])
            listPlace.add(place)
        }
        return listPlace
    }

    private fun setupView() {
        val categoryList = getListCategory()
        val categoryAdapter = CategoryAdapter(categoryList)

        binding.rvPlaces.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = categoryAdapter
        }

        categoryAdapter.setOnItemClickCallback(object : CategoryAdapter.OnItemClickCallback {
            override fun onItemClicked(items: Category) {
                val context = requireContext()
                val intent = when (items.name) {
                    "Tourist Attractions" -> Intent(context, TouristAttractionsActivity::class.java)
                    "Culinary Spot" -> Intent(context, CulinarySpotActivity::class.java)
                    "Lodging" -> Intent(context, LodgingActivity::class.java)
                    else -> null
                }
                intent?.let { startActivity(it) }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}