package com.capstonehore.ngelana.view.mytrip.tabs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.adapter.TripAdapter
import com.capstonehore.ngelana.data.remote.response.PlanUserItem
import com.capstonehore.ngelana.databinding.FragmentCanceledTripBinding
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.home.plan.PlanViewModel
import com.capstonehore.ngelana.view.mytrip.detail.CanceledTripDetailActivity

class CanceledTripFragment : Fragment() {

    private var _binding: FragmentCanceledTripBinding? = null

    private val binding get() = _binding!!

    private lateinit var tripAdapter: TripAdapter

    private lateinit var planViewModel: PlanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCanceledTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        planViewModel = obtainViewModel(requireActivity())

        setupAdapter()
        setupView()
    }

    private fun setupAdapter() {
        tripAdapter = TripAdapter()

        binding.rvPlan.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = tripAdapter
        }

        tripAdapter.setOnItemClickCallback(object : TripAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PlanUserItem?) {
                data?.let {
                    val intent = Intent(requireActivity(), CanceledTripDetailActivity::class.java).apply {
                        putExtra(CanceledTripDetailActivity.EXTRA_PLAN_ITEM, arrayListOf(it))
                    }
                    startActivity(intent)
                }
            }
        })
    }

    private fun setupView() {
        planViewModel.canceledPlans.observe(viewLifecycleOwner) { result ->
            tripAdapter.submitList(result)
        }
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
        private const val TAG = "CanceledTripFragment"
    }
}