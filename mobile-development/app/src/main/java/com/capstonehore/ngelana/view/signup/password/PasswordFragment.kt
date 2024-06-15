package com.capstonehore.ngelana.view.signup.password

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.databinding.CustomAlertDialogBinding
import com.capstonehore.ngelana.databinding.FragmentPasswordBinding
import com.capstonehore.ngelana.view.onboarding.OnboardingActivity
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

            showCustomAlertDialog(true, "")

//            viewModel.allFavorites.observe(viewLifecycleOwner, Observer { result ->
//                when (it) {
//                    is Result.Success -> {
                        //            submitDataToApi(name, email, password)
//                        showCustomAlertDialog(true)
//                    }
//                    is Result.Error -> {
//                        // Show error message
//                        val error = result.error
//                        Log.e(TAG, "Error observing favorites: $error")
//                    }
//                    Result.Loading -> {
//                        // Show loading indicator
//                        Log.d(TAG, "Loading favorites...")
//                    }
//                }
//            })
        }
    }

    private fun setupPassword() {
        signUpViewModel.password.observe(viewLifecycleOwner) { password ->
            binding.edPassword.setText(password)
        }
    }

    private fun showCustomAlertDialog(isSuccess: Boolean, message: String) {
        val inflater = LayoutInflater.from(requireActivity())
        val alertLayout = CustomAlertDialogBinding.inflate(inflater)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(alertLayout.root)

        val dialog = builder.create()
        dialog.show()

        if (isSuccess) {
            alertLayout.alertIcon.setImageResource(R.drawable.ic_check_circle)
            alertLayout.alertTitle.text = getString(R.string.success_completed_title)
            alertLayout.alertMessage.text = getString(R.string.registration_completed_message)

            alertLayout.positiveButton.setOnClickListener {
                moveToInterest()
                dialog.dismiss()
            }
            alertLayout.negativeButton.visibility = View.GONE
        } else {
            alertLayout.alertIcon.setImageResource(R.drawable.ic_error)
            alertLayout.alertTitle.text = getString(R.string.registration_failed)
            alertLayout.alertMessage.text = message

            alertLayout.positiveButton.visibility = View.GONE
            alertLayout.negativeButton.setOnClickListener {
                moveToOnboarding()
                dialog.dismiss()
            }
        }

        // Animation
        val scaleX = ObjectAnimator.ofFloat(alertLayout.alertIcon, "scaleX", 0.5f, 1f)
        val scaleY = ObjectAnimator.ofFloat(alertLayout.alertIcon, "scaleY", 0.5f, 1f)
        val tvTitle = ObjectAnimator.ofFloat(alertLayout.alertTitle, View.ALPHA, 0f, 1f)
        val tvMessage = ObjectAnimator.ofFloat(alertLayout.alertMessage, View.ALPHA, 0f, 1f)
        val positiveButton = ObjectAnimator.ofFloat(alertLayout.positiveButton, View.ALPHA, 0f, 1f)
        val negativeButton = ObjectAnimator.ofFloat(alertLayout.negativeButton, View.ALPHA, 0f, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, tvTitle, tvMessage, positiveButton, negativeButton)
        animatorSet.duration = 500
        animatorSet.start()
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

    private fun moveToOnboarding() {
        startActivity(Intent(requireActivity(), OnboardingActivity::class.java))
        requireActivity().finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}