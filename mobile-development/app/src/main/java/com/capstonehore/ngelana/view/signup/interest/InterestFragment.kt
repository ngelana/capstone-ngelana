package com.capstonehore.ngelana.view.signup.interest

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.InterestAdapter
import com.capstonehore.ngelana.data.local.entity.Interest
import com.capstonehore.ngelana.databinding.FragmentInterestBinding
import com.capstonehore.ngelana.view.login.LoginActivity

class InterestFragment : Fragment() {

    private var _binding: FragmentInterestBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInterestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
        setupAnimation()
        setupView()
    }


    private fun setupAction() {
        binding.skipButton.setOnClickListener {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }
        binding.nextButton.setOnClickListener {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun setupAnimation() {
        val tvTitle = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(300)
        val tvDescription =
            ObjectAnimator.ofFloat(binding.tvDescription, View.ALPHA, 1f).setDuration(300)
        val rvInterest = ObjectAnimator.ofFloat(binding.rvInterest, View.ALPHA, 1f).setDuration(300)
        val nextButton =
            ObjectAnimator.ofFloat(binding.nextButton, View.ALPHA, 1f).setDuration(300)
        val skipButton = ObjectAnimator.ofFloat(binding.skipButton, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(nextButton, skipButton)
        }

        AnimatorSet().apply {
            playSequentially(
                tvTitle,
                tvDescription,
                rvInterest,
                together
            )
            start()
        }
    }

    private fun getListInterest(): ArrayList<Interest> {
        val dataName = resources.getStringArray(R.array.data_interest_name)
        val dataIcon = resources.obtainTypedArray(R.array.data_interest_icon)
        val listInterest = ArrayList<Interest>()
        for (i in dataName.indices) {
            val interest = Interest(dataName[i], dataIcon.getResourceId(i, -1))
            listInterest.add(interest)
        }
        dataIcon.recycle()
        return listInterest
    }

    private fun setupView() {
        val interestList = getListInterest()

        val interestAdapter = InterestAdapter(interestList)

        binding.rvInterest.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = interestAdapter
        }

//        interestAdapter.setOnItemClickCallback(object : InterestAdapter.OnItemClickCallback {
//            override fun onItemClicked(items: Place) {
//            }
//        })
    }
}