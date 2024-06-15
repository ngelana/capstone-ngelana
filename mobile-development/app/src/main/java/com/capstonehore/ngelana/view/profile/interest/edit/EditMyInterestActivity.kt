package com.capstonehore.ngelana.view.profile.interest.edit

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.InterestAdapter
import com.capstonehore.ngelana.data.Interest
import com.capstonehore.ngelana.databinding.ActivityEditMyInterestBinding
import com.capstonehore.ngelana.view.profile.interest.MyInterestActivity

class EditMyInterestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditMyInterestBinding

    private lateinit var interestAdapter: InterestAdapter

    private val selectedItems = SparseBooleanArray()
    private var interestCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMyInterestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupToolbar()
        setupAnimation()
        setupView()
    }

    private fun setupAction() {
        binding.submitButton.setOnClickListener {
            startActivity(Intent(this, MyInterestActivity::class.java))
            finish()
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

    private fun setupAnimation() {
        val tvTitle = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(300)
        val tvDescription =
            ObjectAnimator.ofFloat(binding.tvDescription, View.ALPHA, 1f).setDuration(300)
        val rvInterest = ObjectAnimator.ofFloat(binding.rvInterest, View.ALPHA, 1f).setDuration(300)
        val submitButton =
            ObjectAnimator.ofFloat(binding.submitButton, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(
                tvTitle,
                tvDescription,
                rvInterest,
                submitButton
            )
            start()
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

        interestAdapter = InterestAdapter(interestList, selectedItems) { position ->
            val isSelected = selectedItems[position]
            if (isSelected) {
                selectedItems.delete(position)
            } else {
                selectedItems.put(position, true)
            }
            updateInterestCount()
            interestAdapter.notifyItemChanged(position)
        }

        binding.rvInterest.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@EditMyInterestActivity, 2)
            adapter = interestAdapter
        }
    }

    private fun updateInterestCount() {
        interestCount = selectedItems.size()
        binding.tvInterestCount.text = interestCount.toString()

        if (binding.tvInterestCount.alpha == 0f) {
            binding.tvInterestCount.alpha = 1f
        }
    }

    companion object {
        const val EXTRA_RESULT_INTEREST = "extra_result_interest"
    }
}