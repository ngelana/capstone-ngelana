package com.capstonehore.ngelana.view.mytrip.tabs

import android.content.Context
import android.content.Intent
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
import com.capstonehore.ngelana.adapter.UpcomingTripAdapter
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.PlanUserItem
import com.capstonehore.ngelana.databinding.FragmentUpcomingTripBinding
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.home.plan.PlanViewModel
import com.capstonehore.ngelana.view.mytrip.detail.UpcomingTripDetailActivity

class UpcomingTripFragment : Fragment() {

    private var _binding: FragmentUpcomingTripBinding? = null

    private val binding get() = _binding!!

    private lateinit var upcomingTripAdapter: UpcomingTripAdapter

    private lateinit var planViewModel: PlanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        planViewModel = obtainViewModel(requireActivity())

        setupAdapter()
        setupView()
    }

    private fun setupAdapter() {
        upcomingTripAdapter = UpcomingTripAdapter()

        binding.rvPlan.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = upcomingTripAdapter
        }

        upcomingTripAdapter.setOnItemClickCallback(object : UpcomingTripAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PlanUserItem?) {
                data?.let {
                    val intent = Intent(requireActivity(), UpcomingTripDetailActivity::class.java).apply {
                        putExtra(UpcomingTripDetailActivity.EXTRA_PLAN_ITEM, arrayListOf(it))
                    }
                    startActivity(intent)
                }
            }
        })
    }

    private fun setupView() {
        planViewModel.getPlanByUserId().observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Result.Success -> {
                        showLoading(false)

                        val response = it.data
                        response.let { item ->
                            upcomingTripAdapter.submitList(item)
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
        private const val TAG = "UpcomingTripFragment"
    }
}