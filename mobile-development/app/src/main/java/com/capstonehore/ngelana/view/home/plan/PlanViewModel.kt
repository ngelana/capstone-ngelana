package com.capstonehore.ngelana.view.home.plan

import androidx.lifecycle.ViewModel
import com.capstonehore.ngelana.data.remote.response.PlanUserItem
import com.capstonehore.ngelana.data.repository.PlanRepository

class PlanViewModel(
    private val planRepository: PlanRepository
//    private val repository: Repository
): ViewModel() {

//    fun getRecommendedPlace(date: String) = planRepository.getRecommendedPlace(date)

    fun setPlanResult(planUserItem: PlanUserItem) = planRepository.setPlanResult(planUserItem)

    fun setDetailPlanResult(planUserItem: PlanUserItem) = planRepository.setDetailPlanResult(planUserItem)

    fun getPlanByUserId() = planRepository.getPlanByUserId()

    fun getPlanDetailByUserId() = planRepository.getPlanDetailByUserId()

}