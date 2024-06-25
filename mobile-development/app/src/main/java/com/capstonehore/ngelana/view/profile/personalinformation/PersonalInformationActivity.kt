package com.capstonehore.ngelana.view.profile.personalinformation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.UserInformationItem
import com.capstonehore.ngelana.databinding.ActivityPersonalInformationBinding
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.profile.ProfileViewModel
import com.capstonehore.ngelana.view.profile.personalinformation.edit.EditPersonalInformationActivity

class PersonalInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalInformationBinding

    private lateinit var profileViewModel: ProfileViewModel

    private var userInformationItem: UserInformationItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileViewModel = obtainViewModel(this@PersonalInformationActivity)

        setupAction()
        setupToolbar()
        setupUser()
    }

    private fun setupAction() {
        binding.editButton.setOnClickListener {
            val intent = Intent(
                this@PersonalInformationActivity,
                EditPersonalInformationActivity::class.java
            ).apply {
                putExtra(
                    EditPersonalInformationActivity.EXTRA_RESULT_INFORMATION,
                    userInformationItem
                )
            }
            startActivity(intent)
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

    private fun setupUser() {
        profileViewModel.getUserById().observe(this@PersonalInformationActivity) {
            if (it != null) {
                when (it) {
                    is Result.Success -> {
                        showLoading(false)

                        val response = it.data

                        val name = response.data?.name ?: getString(R.string.set_my_name)
                        val birth =
                            response.data?.birthdate ?: getString(R.string.set_my_date_of_birth)
                        val gender = response.data?.gender ?: getString(R.string.set_my_gender)
                        val phone = response.data?.phone ?: getString(R.string.set_my_phone_number)
                        val email = response.data?.email ?: getString(R.string.set_my_email)

                        userInformationItem = UserInformationItem(name, birth, gender, phone, email)

                        binding.apply {
                            userName.text = name
                            userDateOfBirth.text = birth
                            userGender.text = gender
                            userPhone.text = phone
                            userEmail.text = email
                        }
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showToast(it.error)
                    }
                    is Result.Loading -> showLoading(true)

                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): ProfileViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[ProfileViewModel::class.java]
    }

}