package com.capstonehore.ngelana.view.signup.email

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.databinding.FragmentEmailBinding
import com.capstonehore.ngelana.view.home.HomeViewModel
import com.capstonehore.ngelana.view.signup.name.NameFragment
import com.capstonehore.ngelana.view.signup.password.PasswordFragment

class EmailFragment : Fragment() {

    private var _binding: FragmentEmailBinding? = null

    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmailBinding.inflate(inflater, container, false)
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
                .replace(R.id.main, NameFragment())
                .commit()
        }
        binding.nextButton.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(R.id.main, PasswordFragment())
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
        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(300)
        val nextButton =
            ObjectAnimator.ofFloat(binding.nextButton, View.ALPHA, 1f).setDuration(300)
        val backButton = ObjectAnimator.ofFloat(binding.backButton, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(nextButton, backButton)
        }

        AnimatorSet().apply {
            playSequentially(
                tvTitle,
                tvDescription,
                tvQuestion,
                tvEmail,
                together
            )
            start()
        }
    }

}