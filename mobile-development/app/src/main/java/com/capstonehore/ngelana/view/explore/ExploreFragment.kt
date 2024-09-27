package com.capstonehore.ngelana.view.explore

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.CategoryAdapter
import com.capstonehore.ngelana.data.local.entity.Category
import com.capstonehore.ngelana.databinding.FragmentExploreBinding
import com.capstonehore.ngelana.view.explore.place.culinary.CulinarySpotActivity
import com.capstonehore.ngelana.view.explore.place.lodging.LodgingActivity
import com.capstonehore.ngelana.view.explore.place.tourist.TouristAttractionsActivity

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null

    private val binding get() = _binding!!

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
        val dataCode = resources.getStringArray(R.array.data_category_code)
        val dataName = resources.getStringArray(R.array.data_category_name)
        val dataDescription = resources.getStringArray(R.array.data_category_description)
        val dataImage = resources.getStringArray(R.array.data_category_image)
        val listCategory = ArrayList<Category>()

        for (i in dataName.indices) {
            val place = Category(dataCode[i], dataName[i], dataDescription[i], dataImage[i])
            listCategory.add(place)
        }
        return listCategory
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
            override fun onItemClicked(item: Category?) {
                item?.let {
                    val context = requireContext()
                    val intent = when (item.code) {
                        "NGELANA-TA" -> Intent(context, TouristAttractionsActivity::class.java)
                        "NGELANA-CS" -> Intent(context, CulinarySpotActivity::class.java)
                        "NGELANA-LO" -> Intent(context, LodgingActivity::class.java)
                        else -> null
                    }
                    intent?.let { startActivity(it) }
                }
            }
        })

        categoryAdapter.setOnButtonClickCallback(object : CategoryAdapter.OnButtonClickCallback {
            override fun onButtonClicked(item: Category?) {
                item?.let {
                    val context = requireContext()
                    val intent = when (item.code) {
                        "NGELANA-TA" -> Intent(context, TouristAttractionsActivity::class.java)
                        "NGELANA-CS" -> Intent(context, CulinarySpotActivity::class.java)
                        "NGELANA-LO" -> Intent(context, LodgingActivity::class.java)
                        else -> null
                    }
                    intent?.let { startActivity(it) }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}