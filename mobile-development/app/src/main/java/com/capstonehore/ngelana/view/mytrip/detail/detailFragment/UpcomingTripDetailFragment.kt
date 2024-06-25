package com.capstonehore.ngelana.view.mytrip.detail.detailFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.adapter.PlanResultAdapter
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.databinding.FragmentUpcomingTripDetailBinding
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.detail.DetailPlaceFragment
import com.capstonehore.ngelana.view.home.plan.PlanViewModel

class UpcomingTripDetailFragment : Fragment() {

    private var _binding: FragmentUpcomingTripDetailBinding? = null

    private val binding get() = _binding!!

    private lateinit var planResultAdapter: PlanResultAdapter

    private lateinit var planViewModel: PlanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingTripDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        planViewModel = obtainViewModel(requireActivity())

        setupAdapter()
        setupView()
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
                    val dialogFragment = DetailPlaceFragment.newInstance(data)
                    dialogFragment.show(childFragmentManager, "DetailPlaceFragment")
                }
            }
        })
    }

    private fun setupView() {
        planViewModel.getPlanDetailByUserId().observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Result.Success -> {
                        showLoading(false)

                        val response = it.data
                        response.let { item ->
                            planResultAdapter.submitList(item)
                        }
                        Log.d(TAG, "Successfully Show Upcoming Trip Plan: $response")
                    }
                    is Result.Error -> {
                        showLoading(false)

                        showToast(it.error)
                        Log.d(TAG, "Failed to Show Upcoming Trip Plan: ${it.error}")
                    }
                    is Result.Loading -> showLoading(true)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading:Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun obtainViewModel(activity: FragmentActivity): PlanViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[PlanViewModel::class.java]
    }

    companion object {
        private const val TAG = "UpcomingTripDetailFragment"
    }
}