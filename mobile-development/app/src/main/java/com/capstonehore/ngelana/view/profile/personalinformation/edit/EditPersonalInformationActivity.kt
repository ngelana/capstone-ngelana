package com.capstonehore.ngelana.view.profile.personalinformation.edit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstonehore.ngelana.data.PersonalInformation
import com.capstonehore.ngelana.databinding.ActivityEditPersonalInformationBinding

class EditPersonalInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditPersonalInformationBinding

    private lateinit var personalInformation: PersonalInformation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPersonalInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        personalInformation = intent.getParcelableExtra(EXTRA_RESULT_INFORMATION) ?: return

        setupData(personalInformation)
    }

    private fun setupData(data: PersonalInformation) {
        binding.edName.setText(data.name)
//        binding.edBirth.setText(data.birthDate)
//        if (data.gender == "Female") {
//            binding.rbWoman.isChecked = true
//        } else {
//            binding.rbMan.isChecked = true
//        }
//        binding.edPhone.setText(data.phoneNumber)
//        binding.edEmail.setText(data.email)
    }



    companion object {
        const val EXTRA_RESULT_INFORMATION = "extra_result_information"
    }
}