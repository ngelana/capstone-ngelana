package com.capstonehore.ngelana.view.signup.name

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.databinding.FragmentNameBinding
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.login.LoginActivity
import com.capstonehore.ngelana.view.signup.SignUpViewModel
import com.capstonehore.ngelana.view.signup.email.EmailFragment

class NameFragment : Fragment() {

    private var _binding: FragmentNameBinding? = null

    private val binding get() = _binding!!

    private lateinit var signUpViewModel: SignUpViewModel

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SESSION)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpViewModel = obtainViewModel(requireActivity())

        setupAction()
        setupImage()
        setupAnimation()
        setupButton()
        setupName()
    }

    private fun setupAction() {
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.nextButton.setOnClickListener {
            val name = binding.edName.text.toString()
            if (name.isEmpty()) {
                showToast(getString(R.string.empty_name))
            } else {
                signUpViewModel.setName(name)
                moveToEmail()
            }
        }
    }

    private fun setupImage() {
        val image = "https://storage.googleapis.com/ngelana-bucket/ngelana-assets/img_ngelana_welcome.png"
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
        val tvName = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(300)
        val divider1 = ObjectAnimator.ofFloat(binding.divider1, View.ALPHA, 1f).setDuration(300)
        val tvLogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(300)
        val nextButton =
            ObjectAnimator.ofFloat(binding.nextButton, View.ALPHA, 1f).setDuration(500)
        val backButton = ObjectAnimator.ofFloat(binding.backButton, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(nextButton, backButton)
        }

        AnimatorSet().apply {
            playSequentially(
                imageView,
                tvTitle,
                tvDescription,
                tvQuestion,
                tvName,
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

    private fun setupName() {
        signUpViewModel.name.observe(viewLifecycleOwner) { name ->
            binding.edName.setText(name)
        }
    }

    private fun moveToEmail() {
        parentFragmentManager.beginTransaction()
                .replace(R.id.main, EmailFragment())
                .addToBackStack(null)
                .commit()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: FragmentActivity): SignUpViewModel {
        val factory = ViewModelFactory.getInstance(
            activity.application,
            UserPreferences.getInstance(requireActivity().dataStore)
        )
        return ViewModelProvider(activity, factory)[SignUpViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val SESSION = "session"
    }
}