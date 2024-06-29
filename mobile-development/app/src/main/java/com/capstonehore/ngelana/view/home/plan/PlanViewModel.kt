package com.capstonehore.ngelana.view.home.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstonehore.ngelana.data.remote.response.DataPlacesItem
import com.capstonehore.ngelana.data.remote.response.PlanUserItem
import com.capstonehore.ngelana.data.repository.PlanRepository

class PlanViewModel(
    private val planRepository: PlanRepository
//    private val repository: Repository
) : ViewModel() {

    private val _completedPlans = MutableLiveData<MutableList<PlanUserItem>>()
    val completedPlans: LiveData<MutableList<PlanUserItem>> = _completedPlans

    private val _canceledPlans = MutableLiveData<MutableList<PlanUserItem>>()
    val canceledPlans: LiveData<MutableList<PlanUserItem>> = _canceledPlans

    init {
        _completedPlans.value = mutableListOf()
        _canceledPlans.value = mutableListOf()
    }

    fun addCompletedPlan(plan: PlanUserItem) {
        _completedPlans.value?.add(plan)
        _completedPlans.value = _completedPlans.value
    }

    fun addCanceledPlan(plan: PlanUserItem) {
        _canceledPlans.value?.add(plan)
        _canceledPlans.value = _canceledPlans.value
    }

//    fun getRecommendedPlace(date: String) = planRepository.getRecommendedPlace(date)

    fun setPlanResult(name: String, date: String, places: List<DataPlacesItem>) =
        planRepository.setPlanResult(name, date, places)

    fun getPlanByUserId() = planRepository.getPlanByUserId()

    fun getPlanDetailByUserId() = planRepository.getPlanDetailByUserId()

}