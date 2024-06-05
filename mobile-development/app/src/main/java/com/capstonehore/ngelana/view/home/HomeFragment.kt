package com.capstonehore.ngelana.view.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.databinding.FragmentHomeBinding
import com.capstonehore.ngelana.view.home.plan.recommendation.RecommendationPlanActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
        setupAnimation()
        setupTitle()

    }

    private fun setupAction() {
        binding.submitButton.setOnClickListener {
            startActivity(Intent(requireActivity(), RecommendationPlanActivity::class.java))
        }
    }

    private fun setupAnimation() {
        val tvTitle = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(300)
        val tvDescription =
            ObjectAnimator.ofFloat(binding.tvDescription, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(tvTitle, tvDescription)
            start()
        }
    }

    private fun setupTitle() {
        val blue = ContextCompat.getColor(requireContext(), R.color.blue)

        val spannable =
            SpannableString(getString(R.string.home_title, getString(R.string.plan)))
        spannable.setSpan(
            ForegroundColorSpan(blue),
            spannable.indexOf(getString(R.string.plan)),
            spannable.indexOf(getString(R.string.plan)) + getString(R.string.plan).length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvTitle.text = spannable
    }

}