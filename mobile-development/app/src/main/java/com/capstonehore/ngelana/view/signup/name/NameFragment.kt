package com.capstonehore.ngelana.view.signup.name

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.databinding.FragmentNameBinding
import com.capstonehore.ngelana.view.signup.SignUpViewModel
import com.capstonehore.ngelana.view.signup.email.EmailFragment

class NameFragment : Fragment() {

    private var _binding: FragmentNameBinding? = null

    private val binding get() = _binding!!
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNameBinding.inflate(inflater, container, false)
        signUpViewModel = ViewModelProvider(requireActivity())[SignUpViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
        setupImage()
        setupAnimation()
        setupName()
    }

    private fun setupAction() {
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.nextButton.setOnClickListener {
            val name = binding.edName.text.toString()
            moveToEmail(name)
        }
    }

    private fun setupImage() {
        val image = "https://storage.googleapis.com/ngelana-bucket/ngelana-assets/img_ngelana_welcome.png"
        Glide.with(requireActivity())
            .load(image)
            .into(binding.logoImage)
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
        val tvName = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(300)
        val nextButton =
            ObjectAnimator.ofFloat(binding.nextButton, View.ALPHA, 1f).setDuration(500)
        val backButton = ObjectAnimator.ofFloat(binding.backButton, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(nextButton, backButton)
        }

        AnimatorSet().apply {
            playSequentially(
                tvTitle,
                tvDescription,
                tvQuestion,
                tvName,
                together
            )
            start()
        }
    }

    private fun setupName() {
        signUpViewModel.name.observe(viewLifecycleOwner) { name ->
            binding.edName.setText(name)
        }
    }

    private fun moveToEmail(name: String) {
        if (name.isNotEmpty()) {
            signUpViewModel.setName(name)

            parentFragmentManager.beginTransaction()
                .replace(R.id.main, EmailFragment())
                .addToBackStack(null)
                .commit()
        } else {
            showToast(getString(R.string.empty_name))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}