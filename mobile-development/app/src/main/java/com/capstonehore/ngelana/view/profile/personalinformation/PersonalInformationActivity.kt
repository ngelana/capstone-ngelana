package com.capstonehore.ngelana.view.profile.personalinformation

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.PersonalInformationAdapter
import com.capstonehore.ngelana.data.local.entity.PersonalInformation
import com.capstonehore.ngelana.databinding.ActivityPersonalInformationBinding

class PersonalInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupStatusBar()
        setupView()
    }

    private fun setupStatusBar() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.dark_blue)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                window.statusBarColor = ContextCompat.getColor(this, R.color.white)
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

//        informationAdapter.setOnItemClickCallback(object : PersonalInformationAdapter.OnItemClickCallback {
////            override fun onItemClicked(items: PersonalInformation) {
////                val activity = this@PersonalInformationActivity
////                val intent = when (items.title) {
////                    "Name" -> Intent(activity, TouristAttractionsActivity::class.java)
////                    "Date of Birth" -> Intent(activity, CulinarySpotActivity::class.java)
////                    "Gender" -> Intent(activity, LodgingActivity::class.java)
////                    "No. Handphone" -> Intent(activity, LodgingActivity::class.java)
////                    "Email" -> Intent(activity, LodgingActivity::class.java)
////                    else -> null
////                }
////                intent?.let { startActivity(it) }
////            }
////        })
    }

}