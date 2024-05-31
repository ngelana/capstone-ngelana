package com.capstonehore.ngelana.view.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.PlaceAdapter
import com.capstonehore.ngelana.data.Category
import com.capstonehore.ngelana.databinding.FragmentExploreBinding
import com.capstonehore.ngelana.view.home.HomeViewModel

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null

    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private val list = ArrayList<Category>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPlaces.setHasFixedSize(true)

        list.addAll(getListPlace())
        showRecyclerList()
    }

    private fun getListPlace(): ArrayList<Category> {
        val dataName = resources.getStringArray(R.array.data_category)
        val dataImage = resources.getStringArray(R.array.data_image_category)
        val listPlace= ArrayList<Category>()

        for (i in dataName.indices) {
            val place = Category(dataName[i], dataImage[i])
            listPlace.add(place)
        }
        return listPlace
    }

    private fun showRecyclerList() {
        binding.rvPlaces.layoutManager = LinearLayoutManager(requireActivity())
        val placeAdapter = PlaceAdapter(list)
        binding.rvPlaces.adapter = placeAdapter

        placeAdapter.setOnItemClickCallback(object :
            PlaceAdapter.OnItemClickCallback {
            override fun onItemClicked(items: Category) {
//                val placeDetail =
//                    Intent(this@RecommendationPlanActivity, ProfileFragment::class.java)
////                placeDetail.putExtra(CrewsDetail.EXTRA_CREW, items)
//                startActivity(placeDetail)
            }
        })
    }
}