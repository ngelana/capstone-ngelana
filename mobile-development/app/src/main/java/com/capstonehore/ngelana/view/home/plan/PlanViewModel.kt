package com.capstonehore.ngelana.view.home.plan

import androidx.lifecycle.ViewModel
import com.capstonehore.ngelana.data.remote.response.PlanUserItem
import com.capstonehore.ngelana.data.repository.GeneralRepository

class PlanViewModel(private val repository: GeneralRepository): ViewModel() {

    fun getRecommendedPlace(date: String) = repository.getRecommendedPlace(date)

    fun setPlanResult(planUserItem: PlanUserItem) = repository.setPlanResult(planUserItem)

    fun getPlanByUserId() = repository.getPlanByUserId()

}