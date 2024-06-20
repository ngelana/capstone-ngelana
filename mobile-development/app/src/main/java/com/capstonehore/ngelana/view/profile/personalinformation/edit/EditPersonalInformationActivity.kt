package com.capstonehore.ngelana.view.profile.personalinformation.edit

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.remote.response.UserInformationItem
import com.capstonehore.ngelana.databinding.ActivityEditPersonalInformationBinding
import com.capstonehore.ngelana.databinding.CustomAlertDialogBinding
import com.capstonehore.ngelana.utils.dateFormat
import com.capstonehore.ngelana.utils.obtainViewModel
import com.capstonehore.ngelana.view.profile.ProfileViewModel
import com.capstonehore.ngelana.view.profile.personalinformation.PersonalInformationActivity

class EditPersonalInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditPersonalInformationBinding

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var userInformationItem: UserInformationItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPersonalInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileViewModel = obtainViewModel(ProfileViewModel::class.java) as ProfileViewModel

        @Suppress("DEPRECATION")
        intent.getParcelableExtra<UserInformationItem>(EXTRA_RESULT_INFORMATION)?.let { data ->
            userInformationItem = data
            setupAction()
            setupToolbar()
            setupData(userInformationItem)
        }
    }

    private fun setupAction() {
        binding.submitButton.setOnClickListener {
            updateUser()
        }
    }

    private fun setupToolbar() {
        with(binding) {
            setSupportActionBar(topAppBar)
            topAppBar.setNavigationIcon(R.drawable.ic_arrow_back)
            topAppBar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun setupData(data: UserInformationItem) {
        binding.apply {
            edName.setText(data.name)
            edBirth.setText(data.birthdate)
            rbWoman.isChecked = data.gender == "Female"
            rbMan.isChecked = data.gender != "Female"
            edPhone.setText(data.phone)
            edEmail.setText(data.email)
        }
    }

    private fun updateUser() {
        binding.apply {
            val name = edName.text.toString().trim()
            val birth = edBirth.text.toString().trim()
            val gender = if (rbWoman.isChecked) "Female" else "Male"
            val phone = edPhone.text.toString().trim()
            val email = edEmail.text.toString().trim()

            val userInformationItem = UserInformationItem(
                name,
                birth.dateFormat(),
                gender,
                phone,
                email
            )

            profileViewModel.updateUserById(userInformationItem)
                .observe(this@EditPersonalInformationActivity) {
                    if (it != null) {
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
                                Log.e(TAG, "Error update data: $response")
                            }

                            is Result.Loading -> showLoading(true)
                        }
                    }
                }
        }
    }

    private fun showCustomAlertDialog(isSuccess: Boolean, message: String) {
        val inflater = LayoutInflater.from(this)
        val alertLayout = CustomAlertDialogBinding.inflate(inflater)

        val builder = AlertDialog.Builder(this)
        builder.setView(alertLayout.root)

        val dialog = builder.create()
        dialog.show()

        if (isSuccess) {
            with(alertLayout) {
                alertIcon.setImageResource(R.drawable.ic_check_circle)
                alertTitle.text = getString(R.string.login_success_title)
                alertMessage.text = getString(R.string.user_updated_successfully)

                submitButton.setOnClickListener {
                    moveToPersonalInformation()
                    dialog.dismiss()
                }
            }
        } else {
            with(alertLayout) {
                alertIcon.setImageResource(R.drawable.ic_error)
                alertTitle.text = getString(R.string.login_failed)
                alertMessage.text = getString(R.string.failed_to_update_user, message)

                submitButton.apply {
                    text = getString(R.string.cancel)
                    setBackgroundColor(
                        ContextCompat.getColor(
                            this@EditPersonalInformationActivity,
                            R.color.light_grey
                        )
                    )
                    setTextColor(
                        ContextCompat.getColor(
                            this@EditPersonalInformationActivity,
                            R.color.black
                        )
                    )
                    setOnClickListener {
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

    private fun moveToPersonalInformation() {
        startActivity(
            Intent(
                this@EditPersonalInformationActivity,
                PersonalInformationActivity::class.java
            )
        )
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "EditPersonalInformationActivity"
        const val EXTRA_RESULT_INFORMATION = "extra_result_information"
    }
}