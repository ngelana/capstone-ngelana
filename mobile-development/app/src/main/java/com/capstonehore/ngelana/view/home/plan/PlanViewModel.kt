package com.capstonehore.ngelana.view.home.plan

import androidx.lifecycle.ViewModel
import com.capstonehore.ngelana.data.remote.response.PlanUserItem
import com.capstonehore.ngelana.data.repository.PlanRepository
import com.capstonehore.ngelana.data.repository.Repository

class PlanViewModel(
//    private val planRepository: PlanRepository
    private val repository: Repository
): ViewModel() {

    fun getRecommendedPlace(date: String) = repository.getRecommendedPlace(date)

    fun setPlanResult(planUserItem: PlanUserItem) = repository.setPlanResult(planUserItem)

    fun setDetailPlanResult(planUserItem: PlanUserItem) = repository.setDetailPlanResult(planUserItem)

    fun getPlanByUserId() = repository.getPlanByUserId()

    fun getPlanDetailByUserId() = repository.getPlanDetailByUserId()

}