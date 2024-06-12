package com.capstonehore.ngelana.view.signup.password

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.databinding.FragmentPasswordBinding
import com.capstonehore.ngelana.view.signup.SignUpViewModel
import com.capstonehore.ngelana.view.signup.email.EmailFragment
import com.capstonehore.ngelana.view.signup.interest.InterestFragment

class PasswordFragment : Fragment() {

    private var _binding: FragmentPasswordBinding? = null

    private val binding get() = _binding!!
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        signUpViewModel = ViewModelProvider(requireActivity())[SignUpViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
        setupAnimation()
        setupPassword()

    }

    private fun setupAction() {
        binding.backButton.setOnClickListener {
            moveToEmail()
        }
        binding.submitButton.setOnClickListener {
            setupRegister()
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

    private fun setupRegister() {
        binding.submitButton.setOnClickListener {
            val name = signUpViewModel.name.value
            val email = signUpViewModel.email.value
            val password = binding.edPassword.text.toString()

            if (password.isNotEmpty()) {
                signUpViewModel.setPassword(password)
            } else {
                showToast(getString(R.string.empty_password))
                return@setOnClickListener
            }

//            submitDataToApi(name, email, password)
                moveToInterest()
            }
    }

    private fun setupPassword() {
        signUpViewModel.password.observe(viewLifecycleOwner) { password ->
            binding.edPassword.setText(password)
        }
    }

    private fun moveToEmail() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main, EmailFragment())
            .commit()
    }

    private fun moveToInterest() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main, InterestFragment())
            .commit()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}