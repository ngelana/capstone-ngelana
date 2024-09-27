package com.capstonehore.ngelana.view.mytrip.detail.detailFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.adapter.PlanResultAdapter
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.databinding.FragmentCompletedTripDetailBinding
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.detail.DetailPlaceFragment
import com.capstonehore.ngelana.view.home.plan.PlanViewModel

class CanceledTripDetailFragment : Fragment() {

    private var _binding: FragmentCompletedTripDetailBinding? = null

    private val binding get() = _binding!!

    private lateinit var planResultAdapter: PlanResultAdapter

    private lateinit var planViewModel: PlanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletedTripDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        planViewModel = obtainViewModel(requireActivity())

        setupAdapter()
//        setupView()
    }

    private fun setupAdapter() {
        planResultAdapter = PlanResultAdapter()

        binding.rvPlan.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = planResultAdapter
        }

        planResultAdapter.setOnItemClickCallback(object : PlanResultAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PlaceItem?) {
                data?.let {
                    val dialogFragment = DetailPlaceFragment.newInstance(it)
                    dialogFragment.show(childFragmentManager, "DetailPlaceFragment")
                }
            }
        })
    }

//    private fun setupView() {
//        planViewModel.completedPlans.observe(viewLifecycleOwner) { result ->
//            planResultAdapter.submitList(result)
//        }
//    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: FragmentActivity): PlanViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[PlanViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "CompletedTripDetailFragment"
    }
}