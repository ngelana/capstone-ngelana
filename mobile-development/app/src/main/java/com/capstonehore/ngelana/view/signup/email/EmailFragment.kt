package com.capstonehore.ngelana.view.signup.email

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
import com.capstonehore.ngelana.databinding.FragmentEmailBinding
import com.capstonehore.ngelana.view.signup.SignUpViewModel
import com.capstonehore.ngelana.view.signup.name.NameFragment
import com.capstonehore.ngelana.view.signup.password.PasswordFragment

class EmailFragment : Fragment() {

    private var _binding: FragmentEmailBinding? = null

    private val binding get() = _binding!!
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmailBinding.inflate(inflater, container, false)
        signUpViewModel = ViewModelProvider(requireActivity())[SignUpViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
        setupAnimation()
        setupEmail()

    }

    private fun setupAction() {
        binding.backButton.setOnClickListener {
            moveToName()
        }
        binding.nextButton.setOnClickListener {
            val email = binding.edEmail.text.toString()
            moveToPassword(email)
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

    private fun setupEmail() {
        signUpViewModel.email.observe(viewLifecycleOwner) { email ->
            binding.edEmail.setText(email)
        }
    }

    private fun moveToName() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main, NameFragment())
            .commit()
    }

    private fun moveToPassword(email: String) {
        if (email.isNotEmpty()) {
            signUpViewModel.setEmail(email)

            parentFragmentManager.beginTransaction()
                .add(R.id.main, PasswordFragment())
                .addToBackStack(null)
                .commit()
        } else {
            showToast(getString(R.string.empty_email))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}