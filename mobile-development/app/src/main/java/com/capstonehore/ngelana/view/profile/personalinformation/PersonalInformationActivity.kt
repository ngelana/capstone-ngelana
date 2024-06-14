package com.capstonehore.ngelana.view.profile.personalinformation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.PersonalInformationAdapter
import com.capstonehore.ngelana.data.PersonalInformation
import com.capstonehore.ngelana.databinding.ActivityPersonalInformationBinding
import com.capstonehore.ngelana.view.profile.personalinformation.edit.EditPersonalInformationActivity

class PersonalInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupToolbar()
        setupView()
    }

    private fun setupAction() {
        binding.editButton.setOnClickListener {
            startActivity(Intent(this@PersonalInformationActivity, EditPersonalInformationActivity::class.java))
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

    private fun getListPersonal(): ArrayList<PersonalInformation> {
        val dataTitle = resources.getStringArray(R.array.data_personal_title)
        val dataName = resources.getStringArray(R.array.data_personal_name)
        val personalList = ArrayList<PersonalInformation>()

        for (i in dataName.indices) {
            val personalInformation = PersonalInformation(dataTitle[i], dataName[i])
            personalList.add(personalInformation)
        }
        return personalList
    }

    private fun setupView() {
        val personalInformationList = getListPersonal()
        val informationAdapter = PersonalInformationAdapter(personalInformationList)

        binding.rvUsers.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@PersonalInformationActivity)
            adapter = informationAdapter
        }
    }
}