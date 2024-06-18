package com.capstonehore.ngelana.view.signup.password

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.databinding.CustomAlertDialogBinding
import com.capstonehore.ngelana.databinding.FragmentPasswordBinding
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.login.LoginActivity
import com.capstonehore.ngelana.view.onboarding.OnboardingActivity
import com.capstonehore.ngelana.view.signup.SignUpViewModel
import com.capstonehore.ngelana.view.signup.email.EmailFragment
import com.capstonehore.ngelana.view.signup.interest.InterestFragment

class PasswordFragment : Fragment() {

    private var _binding: FragmentPasswordBinding? = null

    private val binding get() = _binding!!

    private lateinit var signUpViewModel: SignUpViewModel

    private val Context.sessionDataStore by preferencesDataStore(USER_SESSION)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        signUpViewModel = obtainViewModel(requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
        setupImage()
        setupAnimation()
        setupButton()
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

    private fun setupImage() {
        val image = "https://storage.googleapis.com/ngelana-bucket/ngelana-assets/img_ngelana_customize_plan.jpg"
        Glide.with(requireActivity())
            .load(image)
            .into(binding.imageView)
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.logoImage, View.TRANSLATION_X, -70f, 70f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -5f, 5f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val imageView = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(500)
        val tvTitle = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(300)
        val tvDescription =
            ObjectAnimator.ofFloat(binding.tvDescription, View.ALPHA, 1f).setDuration(300)
        val tvQuestion =
            ObjectAnimator.ofFloat(binding.tvQuestion, View.ALPHA, 1f).setDuration(300)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(300)
        val divider1 = ObjectAnimator.ofFloat(binding.divider1, View.ALPHA, 1f).setDuration(300)
        val tvLogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(300)
        val submitButton =
            ObjectAnimator.ofFloat(binding.submitButton, View.ALPHA, 1f).setDuration(300)
        val backButton = ObjectAnimator.ofFloat(binding.backButton, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(submitButton, backButton)
        }

        AnimatorSet().apply {
            playSequentially(
                imageView,
                tvTitle,
                tvDescription,
                tvQuestion,
                tvPassword,
                divider1,
                tvLogin,
                together
            )
            start()
        }
    }

    private fun setupButton() {
        val blue = ContextCompat.getColor(requireContext(), R.color.blue)

        val spannable = SpannableString(
            getString(
                R.string.login_button_from_register,
                getString(R.string.login_here)
            )
        )
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
            }
        }

        val boldSpan = StyleSpan(Typeface.BOLD)
        spannable.setSpan(
            boldSpan,
            spannable.indexOf(getString(R.string.login_here)),
            spannable.indexOf(getString(R.string.login_here)) + getString(R.string.login_here).length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            clickableSpan,
            spannable.indexOf(getString(R.string.login_here)),
            spannable.indexOf(getString(R.string.login_here)) + getString(R.string.login_here).length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(blue),
            spannable.indexOf(getString(R.string.login_here)),
            spannable.indexOf(getString(R.string.login_here)) + getString(R.string.login_here).length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvLogin.text = spannable
        binding.tvLogin.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setupRegister() {
        val name = signUpViewModel.name.value.toString()
        val email = signUpViewModel.email.value.toString()
        val password = binding.edPassword.text.toString()

        if (password.isNotEmpty()) {
            signUpViewModel.setPassword(password)
        } else {
            showToast(getString(R.string.empty_password))
        }

        signUpViewModel.doRegister(name, email, password).observe(viewLifecycleOwner) {
            if( it != null ){
                when (it) {
                    is Result.Success -> {
                        showLoading(false)

                        val response = it.data
                        showCustomAlertDialog(true, "")
                        Log.d(TAG, "Success registering: $response")
                    }
                    is Result.Error -> {
                        showLoading(false)

                        val response = it.error
                        showCustomAlertDialog(false, response)
                        Log.e(TAG, "Error registering: $response")
                    }
                    is Result.Loading -> {
                        showLoading(true)

                        Log.d(TAG, "Loading Register User ....")
                    }
                }
            }
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
            with(alertLayout) {
                alertIcon.setImageResource(R.drawable.ic_check_circle)
                alertTitle.text = getString(R.string.success_completed_title)
                alertMessage.text = getString(R.string.registration_completed_message)

                submitButton.setOnClickListener {
                    moveToInterest()
                    dialog.dismiss()
                }
            }
        } else {
            with(alertLayout) {
                alertIcon.setImageResource(R.drawable.ic_error)
                alertTitle.text = getString(R.string.registration_failed)
                alertMessage.text = message

                submitButton.apply {
                    text = getString(R.string.cancel)
                    setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.light_grey))
                    setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                    setOnClickListener {
                        moveToOnboarding()
                        dialog.dismiss()
                    }
                }
            }
        }

        // Animation
        val scaleX = ObjectAnimator.ofFloat(alertLayout.alertIcon, "scaleX", 0.5f, 1f)
        val scaleY = ObjectAnimator.ofFloat(alertLayout.alertIcon, "scaleY", 0.5f, 1f)
        val tvTitle = ObjectAnimator.ofFloat(alertLayout.alertTitle, View.ALPHA, 0f, 1f)
        val tvMessage = ObjectAnimator.ofFloat(alertLayout.alertMessage, View.ALPHA, 0f, 1f)
        val submitButton = ObjectAnimator.ofFloat(alertLayout.submitButton, View.ALPHA, 0f, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, tvTitle, tvMessage, submitButton)
        animatorSet.duration = 800
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

    private fun showLoading(isLoading:Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: FragmentActivity): SignUpViewModel {
        val factory = ViewModelFactory.getInstance(
            requireContext(),
            UserPreferences.getInstance(activity.sessionDataStore)
        )
        return ViewModelProvider(activity, factory)[SignUpViewModel::class.java]
    }

    companion object {
        private const val TAG = "PasswordFragment"
        const val USER_SESSION = "user_session"
    }
}