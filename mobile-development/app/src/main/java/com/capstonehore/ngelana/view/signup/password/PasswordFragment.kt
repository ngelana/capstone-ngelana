package com.capstonehore.ngelana.view.signup.password

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.databinding.FragmentPasswordBinding
import com.capstonehore.ngelana.view.home.HomeViewModel
import com.capstonehore.ngelana.view.signup.email.EmailFragment
import com.capstonehore.ngelana.view.signup.interest.InterestFragment

class PasswordFragment : Fragment() {

    private var _binding: FragmentPasswordBinding? = null

    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
        setupAnimation()
    }

    private fun setupAction() {
        binding.backButton.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(R.id.main, EmailFragment())
                .commit()
        }
        binding.submitButton.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(R.id.main, InterestFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.logoImage, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val tvTitle = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(300)
        val tvDescription =
            ObjectAnimator.ofFloat(binding.tvDescription, View.ALPHA, 1f).setDuration(300)
        val tvQuestion =
            ObjectAnimator.ofFloat(binding.tvQuestion, View.ALPHA, 1f).setDuration(300)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(300)
        val submitButton =
            ObjectAnimator.ofFloat(binding.submitButton, View.ALPHA, 1f).setDuration(300)
        val backButton = ObjectAnimator.ofFloat(binding.backButton, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(submitButton, backButton)
        }

        AnimatorSet().apply {
            playSequentially(
                tvTitle,
                tvDescription,
                tvQuestion,
                tvPassword,
                together
            )
            start()
        }
    }
}