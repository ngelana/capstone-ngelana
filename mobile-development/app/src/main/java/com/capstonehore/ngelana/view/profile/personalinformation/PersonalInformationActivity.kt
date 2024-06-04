package com.capstonehore.ngelana.view.profile.personalinformation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.PersonalInformationAdapter
import com.capstonehore.ngelana.data.local.entity.PersonalInformation
import com.capstonehore.ngelana.databinding.ActivityPersonalInformationBinding

class PersonalInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalInformationBinding

    private val personalInformationList = ArrayList<PersonalInformation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        personalInformationList.addAll(getListPersonal())
        setupView()
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