package com.capstonehore.ngelana.view.profile.interest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.MyInterestAdapter
import com.capstonehore.ngelana.data.Interest
import com.capstonehore.ngelana.databinding.ActivityMyInterestBinding
import com.capstonehore.ngelana.view.profile.interest.edit.EditMyInterestActivity

class MyInterestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyInterestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInterestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupToolbar()
        setupView()
    }

    private fun setupAction() {
        binding.addInterest.setOnClickListener {
            startActivity(Intent(this@MyInterestActivity, EditMyInterestActivity::class.java))
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

    private fun getListInterest(): ArrayList<Interest> {
        val dataName = resources.getStringArray(R.array.data_interest_name)
        val dataIcon = resources.obtainTypedArray(R.array.data_interest_icon)
        val listInterest = ArrayList<Interest>()
        for (i in dataName.indices) {
            val interest = Interest(dataName[i], dataIcon.getResourceId(i, -1))
            listInterest.add(interest)
        }
        dataIcon.recycle()
        return listInterest
    }

    private fun setupView() {
        val interestList = getListInterest()
        val myInterestAdapter = MyInterestAdapter(interestList)

        binding.rvInterest.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MyInterestActivity)
            adapter = myInterestAdapter
        }

        myInterestAdapter.setOnItemClickCallback(object : MyInterestAdapter.OnItemClickCallback {
            override fun onItemClicked(items: Interest) {
                startActivity(Intent(this@MyInterestActivity, EditMyInterestActivity::class.java)
                    .putExtra(EditMyInterestActivity.EXTRA_RESULT_INTEREST, items))
            }
        })
    }

}