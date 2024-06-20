package com.capstonehore.ngelana.view.home.plan

import androidx.lifecycle.ViewModel
import com.capstonehore.ngelana.data.remote.response.PlanUserItem
import com.capstonehore.ngelana.data.repository.PlanRepository

class PlanViewModel(private val planRepository: PlanRepository): ViewModel() {

    fun getRecommendedPlace(date: String) = planRepository.getRecommendedPlace(date)

    fun setPlanResult(planUserItem: PlanUserItem) = planRepository.setPlanResult(planUserItem)

    fun getPlanByUserId() = planRepository.getPlanByUserId()

}