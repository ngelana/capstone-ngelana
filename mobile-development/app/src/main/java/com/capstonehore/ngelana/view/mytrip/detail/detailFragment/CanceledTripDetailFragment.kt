package com.capstonehore.ngelana.view.mytrip.detail.detailFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.capstonehore.ngelana.adapter.UpcomingTripAdapter
import com.capstonehore.ngelana.databinding.FragmentCompletedTripDetailBinding
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.home.plan.PlanViewModel

class CanceledTripDetailFragment : Fragment() {

    private var _binding: FragmentCompletedTripDetailBinding? = null

    private val binding get() = _binding!!

    private lateinit var upcomingTripAdapter: UpcomingTripAdapter

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


    }

//    private fun setupAdapter() {
//        upcomingTripAdapter = UpcomingTripAdapter()
//
//        binding.rvPlan.apply {
//            setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(requireActivity())
//            adapter = upcomingTripAdapter
//        }
//
//        upcomingTripAdapter.setOnItemClickCallback(object : UpcomingTripAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: PlanUserItem?) {
//                data?.let {
//                    val intent = Intent(requireActivity(), UpcomingTripDetailActivity::class.java).apply {
//                        putExtra(UpcomingTripDetailActivity.EXTRA_PLAN_ITEM, data)
//                    }
//                    startActivity(intent)
//                }
//            }
//        })
//    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
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
        private const val TAG = "CompletedTripDetailFragment"
    }
}